/*
 * FirstPage.java
 *
 * Created on September 24, 2006, 12:57 PM
 */

package backgroundprocessingdemo;

import java.awt.Component;
import java.util.Map;

import javax.swing.JOptionPane;

import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;

/**
 *
 * @author  Tim Boudreau
 */
public class FirstPage extends WizardPage
{

    /** Creates new form FirstPage */
    public FirstPage()
    {
        super("one", "First step");
        initComponents();
    }

    public static String getDescription()
    {
        return "Check the checkbox";
    }

    protected String validateContents(Component component, Object event)
    {
        if (!jCheckBox1.isSelected())
        {
            return "Checkbox must be checked";
        }
        // checkbox2 is tested in the allowNext
        return null;
    }

    public WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard)
    {
        if (jCheckBox2.isSelected())
        {
            JOptionPane.showMessageDialog(this, "Checkbox2 was checked, so next is not allowed");
            return WizardPanelNavResult.REMAIN_ON_PAGE;
        }
        return WizardPanelNavResult.PROCEED;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //GEN-BEGIN:initComponents
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">
    private void initComponents()
    {
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridLayout(2, 1));

        jCheckBox1.setText("Click to enable the Next button");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.setName("checkbox");
        add(jCheckBox1);

        jCheckBox2.setText("Check this to have Next button give error");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox2.setName("jCheckBox2");
        add(jCheckBox2);

    }// </editor-fold>//GEN-END:initComponents

    //GEN-BEGIN:variables
    // Variables declaration - do not modify
    private javax.swing.JCheckBox jCheckBox1;

    private javax.swing.JCheckBox jCheckBox2;
    // End of variables declaration//GEN-END:variables

}
