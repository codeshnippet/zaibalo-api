const utils = require('../js/text-utils');

test('text has no profile references', () => {
  expect(utils.hasProfileReferences('Test text test.')).toBe(false);
});

test('text has profile references', () => {
  expect(utils.hasProfileReferences('Test text @name test.')).toBe(true);
});

test('text without profile references doesn\'t change', () => {
  expect(utils.replaceProfileReferences('Test text name test.', ['name']))
  .toBe('Test text name test.');
});

test('text profile references is replaced by link', () => {
  expect(utils.replaceProfileReferences('Test text @name test.', ['name']))
  .toBe('Test text <a href=\"#/@name\">@name</a> test.');
});

test('all names get replaced by links', () => {
  expect(utils.replaceProfileReferences('Test text @bob and @franky test.', ['bob', 'franky']))
  .toBe('Test text <a href=\"#/@bob\">@bob</a> and <a href=\"#/@franky\">@franky</a> test.');
});

test('shortest match gets replaced', () => {
  expect(utils.replaceProfileReferences('Test @franky sinatra test.', ['franky sinatra', 'franky']))
  .toBe('Test <a href=\"#/@franky\">@franky</a> sinatra test.');
});
