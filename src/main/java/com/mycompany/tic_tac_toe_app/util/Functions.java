package com.mycompany.tic_tac_toe_app.util;

import java.util.function.Supplier;

public class Functions {

    public static void showErrorAlert(final Exception ex) {
        PopUp.showCustom("Something Went Wrong", ex.getLocalizedMessage(), "OK", null, false, null);
    }

    public static void showConfirmAlert(
            final String title,
            final String header,
            final String content,
            final String ok,
            final String cancel,
            final Supplier onOk,
            final Supplier onCancel
    ) {
        PopUp.showCustom(title, content, ok, cancel, true, accepted -> {
            if (accepted) {
                if (onOk != null) onOk.get();
            } else {
                if (onCancel != null) onCancel.get();
            }
        });
    }

    public static void showInformationAlert(final String title, final String content) {
        PopUp.showCustom(title, content, "OK", null, false, null);
    }
}