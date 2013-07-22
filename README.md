The plugin is inspired from the Matrix Reloaded Plugin and allows the user to choose which matrix combinations he
wants to run.

This plugin add a new type of parameter.
When the user build a matrix project with this parameter type he gets a html table with checkbox for each combination.
It gets from the user which combinations he wants to run, and translate it into a groovy condition such as:

axis1=="axis1value1" && axis2=="axis2value1" || axis1=="axis1value2" && axis2=="axis2value2" || ...


This value is used in the combination filter as follow:


Since Jenkins 1.515 (<a href="https://issues.jenkins-ci.org/browse/JENKINS-7285">support parameters in combinations filter</a>):<br/>
new GroovyShell(binding).evaluate(<i><b>parameterName</b></i>)


Before Jenkins 1.515:<br/>
if (Thread.currentThread().getClass().getName().equals('hudson.model.OneOffExecutor')) {str=Thread.currentThread().getCurrentExecutable().getBuildVariables().get('<i><b>parameterName</b></i>');new GroovyShell(binding).evaluate(str)}


