'use strict';

/* Filters */

angular.module('myApp.filters', []).
  filter('hashtagger',['$filter',
      function($filter) {
          return function(text) {
              if (!text) return text;

              //var replacedText = $filter('linky')(text);
              //var replacedText = $sce.trustAsHtml(text);

              // replace #hashtags and send them to twitter
              var replacePattern1 = /(^|\s)#(\w*[a-zA-ZА-Яа-яґїєё_]+\w*)/gim;
              var replacedText = text.replace(replacePattern1, '$1<a href="#/hashtag/$2">#$2</a>');

              // replace @mentions but keep them to our site
              var replacePattern2 = /(^|\s)\@(\w*[a-zA-ZА-Яа-яґїєё_]+\w*)/gim;
              replacedText = replacedText.replace(replacePattern2, '$1<a href="#/user/$2">@$2</a>');

              return replacedText;
          };

      }
  ]);
