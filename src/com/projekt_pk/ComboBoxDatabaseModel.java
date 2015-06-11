package com.projekt_pk;

import javax.swing.*;

public class ComboBoxDatabaseModel extends AbstractListModel implements ComboBoxModel {
    private String[][] selectionList;

    private String selection = null;

    public ComboBoxDatabaseModel(String[][] objectList) {
        super();
        this.selectionList = objectList;
    }

    public Object getElementAt(int index) {
        return selectionList[index][1];
    }

    public int getSize() {
        return selectionList.length;
    }

    public void setSelectedItem(Object anItem) {
        selection = (String) anItem;
    }

    public Object getSelectedItem() {
        return selection;
    }

    public Object getDatabaseId() {
        Object selected = null;
        for (int i = 0; i < selectionList.length; i++) {
            if (selectionList[i][1].equals(selection))
                selected = selectionList[i][0];
        }
        return selected;
    }
}