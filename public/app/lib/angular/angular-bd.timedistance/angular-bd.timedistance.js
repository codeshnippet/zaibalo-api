'use strict';
angular.module('bd.timedistance', []).filter('timeDistance', function () {
  // poor man's i18n
  var LESS_THAN = 'меньше ніж';
  var ABOUT = 'приблизно';
  var ALMOST = 'майже';
  var OVER = 'більше';
  var A = '';
  var MINUTE = [
      'хвилина',
      'хвилини',
      'хвилин'
    ];
  var HOUR = [
      'година',
      'години',
      'годин'
    ];
  var DAY = [
      'день',
      'дні',
      'днів'
    ];
  var MONTH = [
      'місяць',
      'місяці',
      'місяців'
    ];
  var YEAR = [
      'рік',
      'роки',
      'років'
    ];

  var translationSufix = function(number){
    if((number-number%10)%100!=10){
        if(number%10==1){
          return 0;
        } else if(number%10>=2 && number%10<=4){
          return 1;
        } else {
           return 2;
        }
    } else {
        return 2;
    }
  }
  return function (toTime, fromTime) {
    var out = toTime;
    toTime = new Date(toTime);
    if (!isNaN(toTime)) {
      if (!angular.isDefined(fromTime)) {
        fromTime = new Date();
      }
      var distance = Math.abs(fromTime - toTime);
      var distanceInMinutes = Math.round(Math.abs(distance / 60000));
      var distanceInSeconds = Math.round(Math.abs(distance / 1000));
      if (distanceInMinutes <= 1) {
        if (distanceInMinutes === 0) {
          out = LESS_THAN + ' ' + A + ' ' + MINUTE[translationSufix(distanceInMinutes)];
        } else {
          out = distanceInMinutes + ' ' + MINUTE[translationSufix(distanceInMinutes)];
        }
      } else if (distanceInMinutes >= 2 && distanceInMinutes <= 45) {
        out = distanceInMinutes + ' ' + MINUTE[translationSufix(distanceInMinutes)];
      } else if (distanceInMinutes >= 46 && distanceInMinutes <= 1440) {
        var hours = Math.max(Math.round(distanceInMinutes / 60), 1);
        out = ABOUT + ' ' + hours + ' ' + HOUR[translationSufix(hours)];
      } else if (distanceInMinutes >= 1441 && distanceInMinutes <= 43200) {
        var days = Math.max(Math.round(distanceInMinutes / 1440), 1);
        out = days + ' ' + DAY[translationSufix(days)];
      } else if (distanceInMinutes > 43201 && distanceInMinutes <= 86400) {
        var aboutMonths = Math.max(Math.round(distanceInMinutes / 43200), 1);
        out = ABOUT + ' ' + aboutMonths + ' ' + MONTH[translationSufix(aboutMonths)];
      } else if (distanceInMinutes > 86401 && distanceInMinutes <= 525600) {
        var months = Math.max(Math.round(distanceInMinutes / 43200), 1);
        out = months + ' ' + MONTH[translationSufix(months)];
      } else {
        var isLeapYear = function (year) {
          return year % 4 === 0 && year % 100 !== 0 || year % 400 === 0;
        };
        var fromYear = fromTime.getFullYear();
        if (fromTime.getMonth() >= 2) {
          fromYear += 1;
        }
        var toYear = fromTime.getFullYear();
        if (toTime.getMonth() < 2) {
          toYear -= 1;
        }
        var minutesWithLeapOffset = distanceInMinutes;
        if (fromYear > toYear) {
          var leapYears = 0;
          for (var i = fromYear; i <= toYear; i++) {
            if (isLeapYear(i)) {
              leapYears++;
            }
          }
          minutesWithLeapOffset = distanceInMinutes - leapYears * 1440;
        }
        var remainder = minutesWithLeapOffset % 525600;
        var years = Math.floor(minutesWithLeapOffset / 525600);
        if (remainder < 131400) {
          out = ABOUT + ' ' + years + ' ' + YEAR[translationSufix(years)];
        } else if (remainder < 394200) {
          out = OVER + ' ' + years + ' ' + YEAR[translationSufix(years)];
        } else {
          out = ALMOST + ' ' + years + ' ' + YEAR[translationSufix(years)];
        }
      }
    }
    return out;
  };
});
