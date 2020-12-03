// const taxonHelper = require('./taxon-helper');

const taxon = {
  name:"Bromus alopecuros subsp. caroli-henrici"
};

test.skip('Expect taxonName to arrange in the correct manner', () => {
  expect(taxonHelper.taxonName(taxon)).toBe('<i lang="la">Bromus alopecuros </i> <span lang="la">subsp. </span><i lang="la">caroli-henrici</i>');
});