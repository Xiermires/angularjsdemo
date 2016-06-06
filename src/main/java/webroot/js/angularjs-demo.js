var app = angular.module('angularjs-demo', [ 'ui.grid', 'ui.grid.pagination',
		'ui.bootstrap', 'ui.grid.edit' ]);
app
		.controller(
				'demoCtrl',
				function($scope, $http, $uibModal) {

					// define actions
					$scope.refresh = function() {
						$http
								.get(
										'/angularjsdemo/userTasks')
								.then(function(response) {
									$scope.gridOptions.data = response.data;
								});
					};

					$scope.add = function() {
						var modalInstance = $uibModal.open({
							templateUrl : '/partials/add.html',
							controller : 'ModalDemoCtrl'
						});
						
						modalInstance.result.then(function () {
							$scope.refresh();
						});
					};

					// initial load
					$scope.refresh();
					
					// customize grid
					$scope.gridOptions = {
						columnDefs : [
								{
									field : 'eventName'
								},
								{
									field : 'userName',
									enableCellEdit : true
								},
								{
									field : 'taskName',
									cellTooltip : function(row) {
										return row.entity.description;
									}
								},
								{
									field : 'status',
									enableCellEdit : true,
									editDropdownValueLabel : 'status',
									cellFilter : 'mapStatus:editDropdownOptionsArray:editDropdownIdLabel:editDropdownValueLabel:row.entity.relatedobject.name',
									editableCellTemplate : 'ui-grid/dropdownEditor',
									editDropdownOptionsArray : [ {
										id : 1,
										status : 'PENDING'
									}, {
										id : 2,
										status : 'COMPLETED'
									} ]
								} ]
					};
				});

app.filter('mapStatus', function() {
	var statusHash = {
		1 : 'PENDING',
		2 : 'COMPLETED'
	};

	return function(input, map, idField, valueField, initial) {
		if (typeof map !== "undefined") {
			for (var i = 0; i < map.length; i++) {
				if (map[i][idField] == input) {
					return map[i][valueField];
				}
			}
		} else if (initial) {
			return initial;
		}
		return input;
	};
});

app.controller('ModalDemoCtrl', function($scope, $uibModalInstance, $http) {
	
	// define actions
	$scope.save = function() {
		
		$scope.data.status = 'PENDING'; // just created
		
		$http({
            method : 'POST',
            url : '/angularjsdemo/userTasks',
            data : $scope.data
        }).then($uibModalInstance.close());
	};
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss();
	};
});