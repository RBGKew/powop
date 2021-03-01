const pageTitle = require("./page-title");

describe("updatePageTitle", () => {
  beforeEach(() => {
    document.title = "";
  });
  test("adds query to title if they are present", () => {
    pageTitle.updatePageTitle({ basePageTitle: "A" }, [
      { value: "x" },
      { value: "y" },
    ]);
    expect(document.title).toEqual("x, y | A");
  });
  test("uses base page title if no query is present", () => {
    pageTitle.updatePageTitle({ basePageTitle: "A" }, []);
    expect(document.title).toEqual("A");
  });
});
