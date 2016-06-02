var app = angular.module('angularjs-demo', [ 'ui.grid', 'ui.grid.pagination' ]);
app.controller('demoCtrl', function($scope, $http) {

	$http.get('http://localhost:8080/angularjsdemo/userTasks').then(
			function(response) {
				$scope.gridOptions.data = response.data;
			});

	$scope.gridOptions = {
		columnDefs : [ {
			field : 'eventName'
		}, {
			field : 'userName'
		}, {
			field : 'taskName',
			cellTooltip : function(row) {
				return row.entity.description;
			}
		}, {
			field : 'status'
		} ]
	};
});