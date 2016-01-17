/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.document.scanner.components;

import java.util.HashSet;
import java.util.Set;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 *
 * @author richter
 */
public class OCRResultPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private OCRResultPanelFetcher retriever;
    private final Set<OCRResultPanelUpdateListener> updateListeners = new HashSet<>();
    /**
     * Whether the OCR recognition process can be canceled with a dialog control
     * (modal dialog is displayed while the process is in progress)
     */
    private boolean cancelable = true;
    private MessageHandler messageHandler;
    private final String initialValue;

    public OCRResultPanel(OCRResultPanelFetcher retriever,
            String initialValue,
            MessageHandler messageHandler) {
        this(retriever,
                initialValue,
                true,
                messageHandler);
    }

    public OCRResultPanel(OCRResultPanelFetcher retriever,
            String initialValue,
            boolean cancelable,
            MessageHandler messageHandler) {
        this.initComponents();
        this.retriever = retriever;
        this.oCRResultTextArea.setText(initialValue);
        this.cancelable = cancelable;
        this.initialValue = initialValue;
        reset();
    }

    public String retrieveText() {
        return oCRResultTextArea.getText();
    }

    public void addUpdateListener(OCRResultPanelUpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    public void removeUpdateListener(OCRResultPanelUpdateListener updateListener) {
        this.updateListeners.remove(updateListener);
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void reset() {
        if(this.initialValue != null) {
            this.oCRResultTextArea.setText(initialValue);
        }else {
            this.oCRResultTextArea.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        oCRResultButton = new javax.swing.JButton();
        oCRResultTextAreaScrollPane = new javax.swing.JScrollPane();
        oCRResultTextArea = new javax.swing.JTextArea();

        oCRResultButton.setText("OCR recognition");
        oCRResultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oCRResultButtonActionPerformed(evt);
            }
        });

        oCRResultTextArea.setColumns(20);
        oCRResultTextArea.setRows(5);
        oCRResultTextAreaScrollPane.setViewportView(oCRResultTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oCRResultTextAreaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(oCRResultButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oCRResultTextAreaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(oCRResultButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void oCRResultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oCRResultButtonActionPerformed
        String oCRResult;
        if(!cancelable) {
            oCRResult = this.retriever.fetch();
        }else {
            oCRResult = new OCRResultPanelProgressDialog(null, //parent
                    retriever,
                    messageHandler
            ).processOCR();
        }
        if(oCRResult != null) {
            //might be null if fetch has been canceled (this check is
            //unnecessary for non-cancelable processing, but don't care
            this.oCRResultTextArea.setText(oCRResult);
        }
    }//GEN-LAST:event_oCRResultButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton oCRResultButton;
    private javax.swing.JTextArea oCRResultTextArea;
    private javax.swing.JScrollPane oCRResultTextAreaScrollPane;
    // End of variables declaration//GEN-END:variables
}
