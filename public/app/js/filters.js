'use strict';

/* Filters */

angular.module('zabalo-web.filters', []).
  filter('hashtagger',['$filter', 'UserService',
      function($filter, UserService) {
          return function(text) {
              if (!text) return text;

              // replace #hashtags and send them to twitter
              var replacePattern1 = /(^|\s)#(\w*[1-9a-zA-ZА-Яа-яґіїєё'_]+\w*)/gim;
              var replacedText = text.replace(replacePattern1, '$1<a href="#/tag/$2">#$2</a>');

              // replace @mentions but keep them to our site
              if(hasProfileReferences(replacedText)){
                replacedText = replaceProfileReferences(replacedText, UserService.userDisplayNames);
              }

              return replacedText;
          };

      }
  ]);

  var hasProfileReferences = function(text) {
    return text.indexOf('@') !== -1;
  };

  var replaceProfileReferences = function(text, names) {
    names.sort(function(a, b) {
      return a.length - b.length;
    });

    for (var i = 0; i < names.length; i++) {
      var name = '@'.concat(names[i]);
      if (text.indexOf(name) !== -1) {
        text = text.replace(name, '<a href="#/' + name + '">' + name + '</a>');
      }
    }
    return text;
  };
