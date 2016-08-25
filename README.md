Vaadin SliderPanel
==============

Panel that is able to get collapsed and expand. The expand mode lays over the content...

![showcase](assets/showcase.gif)

Main Features
========

* pur css design
* nice collapse/expand animation
* Listener for collapsed/expanded
* schedule future collapse/expand on client side
* customizable:
  * 4 different display modes
  * 3 different tab position
  * easy to change color and style by css
  * 5 pre designed colors (by *SliderPanelStyles*)
  * SliderNavigation element can flow within underlaying content or not
  * configure custom TabSize (need to change css as well!)


Workflow
========

Add the dependency to your pom the GWT inherits will get automatically added by the maven-vaadin-plugin.

```xml
<dependency>
    <groupId>org.vaadin.addons</groupId>
    <artifactId>vaadin-sliderpanel</artifactId>
    <version>${vaadin-sliderpanel-version}</version>
</dependency>
```

```xml
<inherits name="org.vaadin.sliderpanel.WidgetSet" />
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

Version 1.1.x introduced some extra colors that you can use for example:

```java
SliderPanel sliderPanel = new SliderPanel(dummyContent("White Slider", 3), false, SliderMode.TOP);
firstTopSlider.setCaption("White Slider");
firstTopSlider.addStyleName(SliderPanelStyles.COLOR_WHITE);
```

The class **SliderPanelStyles** holds the style options. Further more you can place your custom StyleName in the same way...

Version 1.2.x introduced a fluent builder to configure SliderPanel:

```java
SliderPanel sliderPanel = new SliderPanelBuilder(dummyContent("White Slider", 3))
  .caption("White Slider")
  .mode(SliderMode.TOP)
  .tabPosition(SliderTabPosition.MIDDLE)
  .style(SliderPanelStyles.COLOR_WHITE)
  .build();
```
Version 1.4.x rearranged the sass styling in order to use mixins and allow to overwrite defaults. The Sample contains an example how you can configure your custom styling. Explanation see Styling.

Details to the addon you can find on [Vaadin](https://vaadin.com/directory#addon/sliderpanel)

Styling
=======
```java
// add your stylesheet to UI
// Specify your custom tabSize and add your custom-stylename
new SliderPanelBuilder(dummyContent("Bottom Slider Heading", 5), "Bottom Custom-Style").mode(SliderMode.BOTTOM)
                .tabSize(80)
                .tabPosition(SliderTabPosition.END).style("my-sliderpanel").build()
```

```scss
$tab-long-size-v: 460; // specify the total length of the sliderpanel-navigator
$tab-short-size-v: 80; // height of the navigator
$tab-caption-size-v: 390; // width of caption within navigator

/* relative to org/vaadin/sliderpanel */
@import "../components/sliderpanel";
.my-sliderpanel {
  @include sliderpanel;

  .v-sliderpanel-wrapper {
    .v-sliderpanel-tab {
      // increase font-size of caption
      .v-sliderpanel-caption {
        font-size: 30px;
        line-height: $tab-short-size-v - 6px;
      }
	// increase size of icon
      .v-sliderpanel-icon {
        height: 44px;
        width: 44px;
        line-height: 44px;
        font-size: 32px;
      }
    }
  }
}
```
Finally you need to configure your pom to unpack the  sliperpanel-components into your project and compile your scss file
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-dependency-plugin</artifactId>
	<executions>
		<execution>
			<id>unpack</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>unpack</goal>
			</goals>
			<configuration>
				<artifactItems>
					<artifactItem>
						<groupId>org.vaadin.addons</groupId>
						<artifactId>vaadin-sliderpanel</artifactId>
						<version>1.4.0-SNAPSHOT</version>
						<type>jar</type>
						<includes>**/sliderpanel/components/*.scss</includes>
						<outputDirectory>${basedir}/src/main/resources</outputDirectory>
					</artifactItem>
				</artifactItems>
			</configuration>
		</execution>
	</executions>
</plugin>

<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>exec-maven-plugin</artifactId>
	<version>1.5.0</version>
	<executions>
		<execution>
			<id>stylesheet</id>
			<phase>compile</phase>
			<goals>
				<goal>java</goal>
			</goals>
			<configuration>
				<includeProjectDependencies>true</includeProjectDependencies>
				<includePluginDependencies>true</includePluginDependencies>
				<executableDependency>
					<groupId>com.vaadin</groupId>
					<artifactId>vaadin-sass-compiler</artifactId>
				</executableDependency>
				<mainClass>com.vaadin.sass.SassCompiler</mainClass>
				<arguments>
					<argument>${basedir}/src/main/resources/org/vaadin/sliderpanel/demo/demo.scss</argument>
					<argument>${basedir}/src/main/resources/org/vaadin/sliderpanel/demo/demo.css</argument>
				</arguments>
			</configuration>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>com.vaadin</groupId>
			<artifactId>vaadin-sass-compiler</artifactId>
			<version>0.9.13</version>
		</dependency>
	</dependencies>
</plugin>
```

Layouting
========
![layout-mockup](assets/sliderpanel-layouting.png)
It's important that you take care for the main layout. Use a combination of VerticalLayout and HorizontalLayout. The SliderPosition is only a hint for the component in which direction the slider should get expand. When you place for example as first component a SliderPanel with SliderMode.RIGHT to a HorizontalLayout and afterwards the middleContent with ExpandRatio(1) it will expand outside the visible browserarea.

```java
protected void init(final VaadinRequest vaadinRequest) {
	final HorizontalLayout mainLayout = new HorizontalLayout();
	mainLayout.setSizeFull();
	
	SliderPanel rightSlider = new SliderPanel(new Label("<h2>SliderContent</h2>", ContentMode.HTML), SliderMode.RIGHT);
	rightSlider.setCaption("SliderCaption");
	mainLayout.addComponent(rightSlider);
	
	VerticalLayout middleContent = new VerticalLayout();
	middleContent.setSizeFull();
	middleContent.addComponent(new Label("<h1>MiddleContent</h1><p>Lots of Content ...</p>", ContentMode.HTML));
	
	mainLayout.addComponent(middleContent);
	mainLayout.setExpandRatio(middleContent, 1);
	setContent(mainLayout);
}
```
![wrong-sample](assets/sliderpanel-wrong-sample.png)

The Layouting of the SliderPanel is not made absolute because of the possibility to use it also within the layout:

![layout-variant](assets/sliderpanel-layouting-variant.png)


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

