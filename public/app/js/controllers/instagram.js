angular.module('myApp.controllers')
    .controller("InstagramController", function($scope, $http) {
      $scope.pics = [];
      $scope.have = [];
      $scope.orderBy = "-likes.count";
      $scope.getMore = function() {
		var endPoint = "https://api.instagram.com/v1/tags/zaibalo.com.ua/media/recent?client_id=e3c2ae0fb7904532bfa625cb0f272e99&callback=JSON_CALLBACK&count=12";
		$http.jsonp(endPoint).success(function(response) {
			for(var i=0; i<response.data.length; i++) {
				if (typeof $scope.have[response.data[i].id]==="undefined") {
					$scope.pics.push(response.data[i]) ;
					$scope.have[response.data[i].id] = "1";
				}
			}
       });
      };
	  
      $scope.getMore();
    });