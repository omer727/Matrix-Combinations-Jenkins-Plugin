package hudson.plugins.matrix_configuration_parameter.matrixconfigurationparameterDefinition

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
            table(border: "1", class: "middle-align center-align", id: "configuration-matrix") {

            drawTableHeader(layouter)
            //Y-axis
            drawTableBody(layouter, axes, nameIt, project)


            }//table
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

private void drawTableBody(Layouter layouter,AxisList axes,String nameIt,MatrixProject project) {
    for (row in layouter.rows) {
        tr() {
            int i = 0;
            for (y in layouter.y) {
                if (row.drawYHeader(i) != null) {
                    td(rowspan: layouter.height(i), class: "matrix-leftcolumn") { raw(row.drawYHeader(i)) }

                }

                i++;
            }
            for (c in row) {
                td() {
                    for (p in c) {
                        div() {
                            drawMainBall(p, axes, nameIt, project);
                        }


                    }


                }

            }
        }
    }
}






private Object drawTableHeader(Layouter layouter) {
    int i = 0;
    for (x in layouter.x) {
        tr(class: "matrix-row") {
            if (!layouter.y.isEmpty()) {
                td(colspan: +layouter.y.size(), id: "matrix-title") { raw("Configuration Matrix") }
            }
            for (row in 1..layouter.repeatX(i)) {
                for (axis in x.values) {
                    td(class: "matrix-header", colspan: layouter.width(i)) { raw(axis) }
                }
            }
        }
        i++;
    }

}
private void drawMainBall(Combination combination,AxisList axes,String matrixName,MatrixProject project) {

    lastBuild = project.getLastBuild();
    if (lastBuild != null && lastBuild.getRun(combination)!=null){
        lastRun = lastBuild.getRun(combination);
        if (lastRun != null){
            a(href:request.getRootPath()+"/"+lastRun.getUrl()){
            img(src: "${imagesURL}/24x24/"+lastRun.getBuildStatusUrl())
            f.checkbox(checked: "false", name: "values",id: "checkbox"+matrixName, value: lastRun.getResult().ordinal)
            input(type: "hidden", name: "confs", value: combination.toString())
            }

        }

    } else{
        img(src: "${imagesURL}/24x24/grey.gif")
        f.checkbox(checked: "false", name: "values",id: "checkbox"+matrixName, value: combination.toIndex((AxisList) axes))
        input(type: "hidden", name: "confs", value: combination.toString())
    }

}