Vaadin SliderPanel
==============

Panel that is able to get collapsed and expand. The expand mode lays over the content...

![showcase](showcase.gif)


Workflow
========

Add the dependency to your pom and add it in the GWT inherits.

```xml
<dependency>
    <groupId>org.vaadin.addons</groupId>
    <artifactId>vaadin-sliderpanel</artifactId>
    <version>${vaadin-sliderpanel-version}</version>
</dependency>
```

```xml
<inherits name="org.vaadin.sliderpanel.Widgetset" />
```

You can recolor the panel by changing the css like:

```css
.v-sliderpanel-wrapper .v-sliderpanel-content {
  background: #99CCFF;
  color: #000;
}
.v-sliderpanel-wrapper .v-sliderpanel-tab {
  background: #B1E7FF;
  color: #000;
}
```

Details to the addon you can find on [Vaadin](https://vaadin.com/directory#addon/sliderpanel)

The MIT License (MIT)
-------------------------

Copyright (c) 2015 Non-Rocket-Science.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

