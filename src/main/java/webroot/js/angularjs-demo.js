var app = angular.module('angularjs-demo', [ 'ui.grid', 'ui.grid.pagination',
		'ui.bootstrap', 'ui.grid.edit', 'ui.grid.selection' ]);
app.controller('demoCtrl', function($scope, $http, $uibModal, $window) {

	$scope.handleError = function(response) {
		if (response.status == 409) {
			$window.alert("Already assigned. "); 
		}	
		else {
			$window.alert(response.statusText);
		}	
	}
	
	$scope.refresh = function() {
		$http.get('/angularjsdemo/userTasks/' + $scope.userName).then(
				function(response) {
					$scope.gridOptions.data = response.data;
				});
	};

	$scope.add = function() {
		var modalInstance = $uibModal.open({
			templateUrl : '/partials/add.html',
			controller : 'ModalDemoCtrl'
		});

		modalInstance.result.then(function() {
			$scope.refresh();
		});
	};
	
	$scope.assign = function() {
		$scope.update($scope.gridApi.selection.getSelectedRows()[0], $scope.userName);
	};
	
	$scope.update = function(data, userName) {
		if (userName) {
			data.userName = userName;
			if (data.status == 1) {
				data.status = 'PENDING';
			}
			else if (data.status == 2) {
				data.status = 'COMPLETED';
			}
			
			$http({
				method : 'POST',
				url : '/angularjsdemo/userTasks',
				data : data
			}).then(function successCallback() {
			}, function errorCallback(response) {
				$scope.handleError(response);
			});
			$scope.refresh();
		}
	};
	
	// initial load, show all.
	$scope.userName = '';
	$scope.refresh();

	// customize grid
	$scope.gridOptions = {
		columnDefs : [
				{
					field : 'eventName',
					displayName: "Event",
					enableCellEdit : false
				},
				{
					field : 'taskName',		
					displayName: "Task",			
					enableCellEdit : false,
					cellTooltip : function(row) {
						return row.entity.description;
					}
				},
				{
					field : 'userName',
					displayName: "User",
					enableCellEdit : false
				},
				{
					field : 'status',
					displayName: "Status",
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
				} ],
		onRegisterApi : function(gridApi) {
			$scope.gridApi = gridApi;
			
			$scope.gridApi.edit.on.afterCellEdit($scope, function(rowEntity) {
				$scope.update(rowEntity, rowEntity.userName);
			});
		}
	};
	
	$scope.gridOptions.multiSelect = false;
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
		if ($scope.data) {
			// not assigned
			$scope.data.userName = '-'; 
			$scope.data.status = 'PENDING'; 

			$http({
				method : 'POST',
				url : '/angularjsdemo/userTasks',
				data : $scope.data
			}).then($uibModalInstance.close());
		}
		else {
			$scope.message = "Error. Empty form.";
		}
	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss();
	};
});