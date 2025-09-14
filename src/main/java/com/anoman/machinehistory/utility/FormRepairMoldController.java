package com.anoman.machinehistory.utility;

import com.anoman.machinehistory.model.problem.Problem;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class FormRepairMoldController {
    @Getter
    public BorderPane panePrintRepair;
    public Label tfCode;
    public Label tfdate;
    public Label tfMold;
    public Label tfSeriMold;
    public Label tfPart;
    public Label tfProblem;
    public Label tfCatatan;


    public void setData(Problem problem) {
        tfCode.setText(problem.getCodeProblem());
        tfdate.setText(String.valueOf(ConvertionMilistoDate.milistoLocalDate(problem.getProblemDate())));
        tfMold.setText(problem.getProductMold().getProduct().getName() + " ( " + problem.getProductMold().getDescription() + " ) ");
        tfSeriMold.setText(problem.getProductMold().getMold().getSerialNumber());
        tfPart.setText(problem.getPart());
        tfProblem.setText(problem.getProblem());
        tfCatatan.setText(problem.getDescriptionProblem());
    }
}
