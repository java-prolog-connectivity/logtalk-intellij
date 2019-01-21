Overview
--------

This plugin provides syntax highlighting for Logtalk and some basic editing
features for IntelliJ IDEA:

- Parentheses matching.
- Multiline terms and comments folding and unfolding.
- Automatic line and block commenting/uncommenting of selection using IntelliJ standard shortcuts.

More information at the [plugin site](https://plugins.jetbrains.com/idea/plugin/9425-logtalk/).


Install or update
-----------------

To install the latest version of this plugin in IntelliJ IDEA follow these steps:

- Go to the IntelliJ IDEA preferences.
- Select *Plugins* and then click on the *Browse Repositories ...* button.
- Type *logtalk* in the search box to find it. Select it and click on *Install* or *Update*.


Known issues
------------

- The grammar still needs some work. Particularly, it does not show as errors
few sentences that should not be considered valid.

- Syntax errors may occur in well constructed terms that are very long 
(e.g., a compound with a huge argument list or a list term with lots of elements).
To fix it, [set the JVM option](https://intellij-support.jetbrains.com/hc/en-us/articles/206544869-Configuring-JVM-options-and-platform-properties) _grammar.kit.gpub.max.level_ to a bigger value than its default _1000_.
