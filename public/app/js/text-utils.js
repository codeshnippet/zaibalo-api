
var utils = {
  hasProfileReferences: function(text) {
    return text.indexOf('@') !== -1;
  },
  replaceProfileReferences: function(text, names) {
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
  }
}

module.exports = utils;
