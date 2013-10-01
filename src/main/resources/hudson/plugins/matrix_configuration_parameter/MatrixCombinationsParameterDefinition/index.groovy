package hudson.plugins.matrix_configuration_parameter.matrixcombinationparameterDefinition

import hudson.matrix.AxisList
import hudson.matrix.Combination
import hudson.matrix.Layouter
import hudson.matrix.MatrixBuild
import hudson.matrix.MatrixProject
import lib.LayoutTagLib
import org.kohsuke.stapler.jelly.groovy.Namespace

l = namespace(LayoutTagLib)
t = namespace("/lib/hudson")
st = namespace("jelly:stapler")
f = namespace("lib/form")
nsProject = namespace("/lib/hudson/project")


MatrixProject project = request.findAncestorObject(MatrixProject.class);
if (project == null)   //in case project is not a Matrix Project
    return;

AxisList axes =  project.getAxes();
String nameIt = it.getName();
Layouter layouter = axes == null ? null : new Layouter<Combination>(axes) {
    protected Combination getT(Combination c) {
        return c;
    }
};





drawMainBody(f, nameIt, axes, project, layouter)

private void drawMainBody(Namespace f, String nameIt, AxisList axes,MatrixProject project,Layouter layouter) {

    drawMainLinksJS(nameIt)


    f.entry(title: nameIt, description: it.getDescription()) {
        div(name: "parameter") {
            input(type: "hidden", name: "name", value: nameIt)
            nsProject.matrix(it: project) {
              drawMainBall(p.combination, project.axes, nameIt, project, layouter);
            }
            raw("<span style=\"font-weight:bold\">Select: </span> \n" +
                "<a href=\"#\" onclick=\"click2Change(0);\">Successful</a> - \n" +
                "<a href=\"#\" onclick=\"click2Change(2);\">Failed</a> - \n" +
                "<a href=\"#\" onclick=\"click2Change(1000);\">All</a> - \n" +
                "<a href=\"#\" onclick=\"click2Change(-1);\">None</a>")

        }//div
    }
}

private void drawMainLinksJS(String nameIt) {
    raw("<script>\n" +
            "function click2Change( status )\n" +
            "{\n" +
            "var i;\n" +
            "for( i = 0, len = document.parameters.elements.length ; i < len ; i++ )\n" +
            "{\n" +
            "var element = document.parameters.elements[i];\n" +
            "if( element.type == 'checkbox' && element.id == \"checkbox" + nameIt + "\" )\n" +
            "{\n" +
            "if( element.value == status || status > 999 )\n" +

            "{\n" +
            "element.checked = true;\n" +
            "}\n" +
            "else\n" +
            "{\n" +
            "element.checked = false;\n" +
            "}\n" +
            "}\n" +
            "}\n" +
            "return false;\n" +
            "}\n" +
            "</script>\n")
}

private void drawMainBall(Combination combination,AxisList axes,String matrixName,MatrixProject project,Layouter layouter) {

    lastBuild = project.getLastBuild();
    if (lastBuild != null && lastBuild.getRun(combination)!=null){
        lastRun = lastBuild.getRun(combination);
        if (lastRun != null){
            a(href:request.getRootPath()+"/"+lastRun.getUrl()){
            img(src: "${imagesURL}/24x24/"+lastRun.getBuildStatusUrl())
            if (!layouter.x || !layouter.y) {
              text(combination.toString(layouter.z))
            }
            }
            checked = combination.evalGroovyExpression(axes, project.combinationFilter)
            f.checkbox(checked: checked, name: "values",id: "checkbox"+matrixName)
            input(type: "hidden", name: "confs", value: combination.toString())

        }

    } else{
        img(src: "${imagesURL}/24x24/grey.gif")
        if (!layouter.x || !layouter.y) {
          text(combination.toString(layouter.z))
        }
        
        checked = combination.evalGroovyExpression(axes, project.combinationFilter)
        f.checkbox(checked: checked, name: "values",id: "checkbox"+matrixName, value: combination.toIndex((AxisList) axes))
        input(type: "hidden", name: "confs", value: combination.toString())
    }

}