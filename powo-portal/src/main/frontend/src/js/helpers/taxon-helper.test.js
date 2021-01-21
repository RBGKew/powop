const Handlebars = require("handlebars");
require("./taxon-helper");

describe("nameAndAuthor", () => {
  test("handles simple name", () => {
    const taxon = {
      name: "Bromus alopecuros",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    expect(template({ taxon })).toEqual('<em lang="la">Bromus alopecuros</em>');
  });

  test("handles name with single rank", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em>'
    );
  });

  test("handles name with multiple ranks", () => {
    const taxon = {
      name: "Bromus hordeaceus subsp. bicuspis var. leiostachys",
    };
    const template = Handlebars.compile("{{nameAndAuthor taxon}}");
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus hordeaceus</em> subsp. <em lang="la">bicuspis</em> var. <em lang="la">leiostachys</em>'
    );
  });

  test("handles author", () => {
    const taxon = {
      name: "Bromus hordeaceus subsp. bicuspis var. leiostachys",
      author: "Philipson & B.C.Stone",
    };
    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    expect(template({ taxon })).toEqual(
      '<em lang="la">Bromus hordeaceus</em> subsp. <em lang="la">bicuspis</em> var. <em lang="la">leiostachys</em> <cite>Philipson & B.C.Stone</cite>'
    );
  });

  test("name with rank which contains another rank", () => {
    const taxon = {
      name: "Senna artemisioides nothosubsp. sturtii",
    };

    const template = Handlebars.compile("{{ nameAndAuthor taxon }}");
    expect(template({ taxon })).toEqual(
      '<em lang="la">Senna artemisioides</em> nothosubsp. <em lang="la">sturtii</em> <cite>(R.Br.) Randell</cite>'
    );
  });
});

describe("taxonLink", () => {
  test("handles simple name", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
      url: "urn:lsid:ipni.org:names:1-1",
    };
    const template = Handlebars.compile("{{ taxonLink taxon }}");
    expect(template({ taxon })).toEqual(
      '<a href="urn:lsid:ipni.org:names:1-1"><em lang="la">Bromus alopecuros</em> subsp. <em lang="la">caroli-henrici</em>'
    );
  });

  test("handles author", () => {
    const taxon = {
      name: "Bromus alopecuros subsp. caroli-henrici",
      url: "urn:lsid:ipni.org:names:1-1",
      author: "Philipson & B.C.Stone",
    };
    const template = Handlebars.compile("{{ taxonLink taxon }}");
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
    expect(template({ taxon })).toEqual("s-theme-Infraspecific");
  });

  test("handles accepted taxon with rank", () => {
    const taxon = {
      accepted: true,
      rank: "family",
    };
    const template = Handlebars.compile("{{ color-theme taxon }}");
    expect(template({ taxon })).toEqual("s-theme-family");
  });

  test("handles not accepted taxon", () => {
    const taxon = {
      accepted: false,
      rank: "subspecies",
    };
    const template = Handlebars.compile("{{ color-theme taxon }}");
    expect(template({ taxon })).toEqual("s-theme-Synonym");
  });
});
