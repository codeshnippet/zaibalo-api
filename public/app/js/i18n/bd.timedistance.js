'use strict';

angular.module('bd.timedistance', [])
  .filter('timeDistance', function () {
    // poor man's i18n

    var enUS = {
      LESS_THAN_A : 'less than a {0}',
      NUMBER_OF_UNITS : '{0} {1}',
      ABOUT_NUMBER_OF_UNITS : 'about {0} {1}',
      ALMOST_NUMBER_OF_UNITS : 'almost {0} {1}',
      OVER_NUMBER_OF_UNITS : 'over {0} {1}',
      MINUTE : ['minute', 'minutes'],
      HOUR : ['hour', 'hours'],
      DAY : ['day','days'],
      MONTH : ['month','months'],
      YEAR : ['year','years'],
      getTranslationIndex : function(number){return number <= 1 ? 0 : 1;}
    };

    var ukUA = {
      LESS_THAN_A : 'меньше ніж {0} тому',
      NUMBER_OF_UNITS : '{0} {1} тому',
      ABOUT_NUMBER_OF_UNITS : 'приблизно {0} {1} тому',
      ALMOST_NUMBER_OF_UNITS : 'майже {0} {1} тому',
      OVER_NUMBER_OF_UNITS : 'більше ніж {0} {1} тому',
      MINUTE : ['хвилина', 'хвилини', 'хвилин'],
      HOUR : ['година', 'години', 'годин'],
      DAY : ['день','дні', 'днів'],
      MONTH : ['місяць','місяці', 'місяців'],
      YEAR : ['рік', 'роки', 'років'],
      getTranslationIndex : function(number){
        if((number-number%10)%100!==10){
          if(number%10===1){
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
    };

    var localeSet = {
      'en_US' : enUS,
      'uk_UA' : ukUA
    };

    return function (toTime,localeName) {

      var locale = localeSet['en_US'];
      if(localeName !== undefined){
        locale = localeSet[localeName];
      }

      var out = toTime;
      toTime = new Date(toTime);
      if (!isNaN(toTime)) {
        var fromTime = new Date();
        var distance = Math.abs(fromTime - toTime);
        var distanceInMinutes = Math.round(Math.abs(distance / 60000.0));
        var distanceInSeconds = Math.round(Math.abs(distance / 1000.0));
        if (distanceInMinutes <= 1) {
          if (distanceInMinutes === 0) {
            out = locale.LESS_THAN_A.fillWith([locale.MINUTE[0]]);
          } else {
            out = locale.NUMBER_OF_UNITS.fillWith([distanceInMinutes, locale.MINUTE[0]]);
          }
        } else if (distanceInMinutes >= 2 && distanceInMinutes <= 45) {
          out = locale.NUMBER_OF_UNITS.fillWith([distanceInMinutes, locale.MINUTE[locale.getTranslationIndex(distanceInMinutes)]]);
        } else if (distanceInMinutes >= 46 && distanceInMinutes <= 1440) {
          var hours = Math.max(Math.round(distanceInMinutes/60.0),1);
          out = locale.ABOUT_NUMBER_OF_UNITS.fillWith([hours, locale.HOUR[locale.getTranslationIndex(hours)]]);
        } else if (distanceInMinutes >= 1441 && distanceInMinutes <= 43200) {
          var days = Math.max(Math.round(distanceInMinutes/1440.0),1);
          out = locale.NUMBER_OF_UNITS.fillWith([days, locale.DAY[locale.getTranslationIndex(days)]]);
        } else if (distanceInMinutes > 43201 && distanceInMinutes <= 86400) {
          var aboutMonths = Math.max(Math.round(distanceInMinutes/43200.0),1);
          out = locale.ABOUT_NUMBER_OF_UNITS.fillWith([aboutMonths, locale.MONTH[locale.getTranslationIndex(aboutMonths)]]);
        } else if (distanceInMinutes > 86401 && distanceInMinutes <= 525600) {
          var months = Math.max(Math.round(distanceInMinutes/43200.0),1);
          out = locale.NUMBER_OF_UNITS.fillWith([months, locale.MONTH[locale.getTranslationIndex(months)]]);
        } else {
          var isLeapYear = function(year) {
            return ((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0);
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
            minutesWithLeapOffset = distanceInMinutes - (leapYears * 1440);
          }
          var remainder = minutesWithLeapOffset % 525600;
          var years = Math.floor(minutesWithLeapOffset / 525600);
          if (remainder < 131400) {
            out = locale.ABOUT_NUMBER_OF_UNITS.fillWith([years, locale.YEAR[locale.getTranslationIndex(years)]]);
          } else if (remainder < 394200) {
            out = locale.OVER_NUMBER_OF_UNITS.fillWith([years, locale.YEAR[locale.getTranslationIndex(years)]]);
          } else {
            out = locale.ALMOST_NUMBER_OF_UNITS.fillWith([years + 1, locale.YEAR[locale.getTranslationIndex(years + 1)]]);
          }
        }
      }
      return out;
    };
  });

String.prototype.fillWith = function(params){
  return this.replace(
      /\{([0-9]+)\}/g,
      function (_, index) { return params[index]; });
};
