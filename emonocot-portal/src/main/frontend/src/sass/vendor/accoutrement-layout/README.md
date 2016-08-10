Accoutrement-Layout
===================

Sass layout [Accoutrement][accoutrement]
by [OddBird][oddbird].
Provides layout utilities
such as media-query helpers,
a float clearfix,
global box-sizing,
positioning shortcuts,
and fluid aspect ratios.

[accoutrement]: http://oddbird.net/accoutrement/
[oddbird]: http://oddbird.net/


Quick Start
-----------

```bash
npm install accoutrement-layout
```

Import the library:

```scss
@import 'path/to/accoutrement-layout/sass/layout'
```

Establish your media-query breakpoints,
or use sizes from [Accoutrement-Scale][scale] directly:

```scss
$sizes: (
  'page': 36rem,
);

$breakpoints: (
  'banner-text': 24em,
);
```

Access your breakpoints with
`above()`, `below()`, and `between()` mixins.
When possible `em` and `rem` units will be converted to
browser-default relative `em` sizes:

```
.banner-text {
  display: none;

  @include above('banner-text') {
    display: block;
  }
}

.container {
  @include below('page') {
    padding: .5em;
  }
}
```

[scale]: http://oddbird.net/accoutrement-scale/sassdoc/
