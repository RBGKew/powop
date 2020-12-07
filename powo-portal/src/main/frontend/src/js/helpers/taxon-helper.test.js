const Handlebars = require("handlebars");
require("./taxon-helper");

describe("nameAndAuthor", () => {
  test.todo("handles simple name");

  test("handles name with single rank", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em>'
    );
  });

  test("handles name with multiple ranks", () => {
    const taxon = {
      name: "Bromus hordeaceus subsp. bicuspis var. leiostachys",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus hordeaceus</em> subsp. <em lang="la">bicuspis</em> var. <em lang="la">leiostachys</em>'
    );
  });

  test.todo("handles author");
});

describe("taxonLink", () => {
  test.todo("handles simple name");

  test.todo("handles author");
});

describe("color-theme", () => {
  test.todo("handles accepted taxon");

  test.todo("handles accepted taxon with rank");

  test.todo("handles not accepted taxon");
});
