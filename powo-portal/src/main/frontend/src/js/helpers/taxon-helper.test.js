const Handlebars = require("handlebars");
require("./taxon-helper");

describe("nameAndAuthor", () => {
  test.todo("handles simple name");

  test.skip("handles name with single rank", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em>'
    );
  });

  test.skip("handles name with multiple ranks", () => {
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
  test.skip("handles simple name", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
      url: "urn:lsid:ipni.org:names:1-1",
    };
    const template = Handlebars.compile("{{ taxonLink taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      '<a href="urn:lsid:ipni.org:names:1-1"><em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em>'
    );
  });

  test.skip("handles author", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
      url: "urn:lsid:ipni.org:names:1-1",
      author: "Philipson & B.C.Stone"
    };
    const template = Handlebars.compile("{{ taxonLink taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      '<a href="urn:lsid:ipni.org:names:1-1"><em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em> <cite>Philipson & B.C.Stone</cite></a>'
    );
  });
});

describe("color-theme", () => {
  test("handles accepted taxon", () => {
    const taxon = {
      accepted: true,
      rank: "subspecies",
    };
    const template = Handlebars.compile("{{ color-theme taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      's-theme-Infraspecific'
    );
  });

  test("handles accepted taxon with rank", () => {
    const taxon = {
      accepted: true,
      rank: "family",
    };
    const template = Handlebars.compile("{{ color-theme taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      's-theme-family'
    );
  });

  test("handles not accepted taxon", () => {
    const taxon = {
      accepted: false,
      rank: "subspecies",
    };
    const template = Handlebars.compile("{{ color-theme taxon }}");
    console.log(template({ taxon }));
    expect(template({ taxon })).toEqual(
      's-theme-Synonym'
    );
  });
});
