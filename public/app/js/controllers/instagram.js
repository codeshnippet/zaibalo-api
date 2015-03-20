angular.module('myApp.controllers')
    .controller("InstagramController", function($scope, $http) {
      $scope.pics = [];
      $scope.have = [];
      $scope.orderBy = "-likes.count";
      $scope.getMore = function() {
		var endPoint = "https://api.instagram.com/v1/tags/zaibalo/media/recent?client_id=e3c2ae0fb7904532bfa625cb0f272e99";
		$http.get(endPoint).
			success(function(data) {
				for(var i=0; i<data.length; i++) {
					if (typeof $scope.have[data[i].id]==="undefined") {
						$scope.pics.push(data[i]) ;
						$scope.have[data[i].id] = "1";
					}
				}
			});
      };
      $scope.getMore();
      
        $scope.tags = [
            'Bootstrap', 'AngularJS', 'Instagram', 'Factory'
        ]
    });