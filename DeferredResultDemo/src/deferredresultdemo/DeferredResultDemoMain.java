/*  The contents of this file are subject to the terms of the Common Development
and Distribution License (the License). You may not use this file except in
compliance with the License.
    You can obtain a copy of the License at http://www.netbeans.org/cddl.html
or http://www.netbeans.org/cddl.txt.
    When distributing Covered Code, include this CDDL Header Notice in each file
and include the License file at http://www.netbeans.org/cddl.txt.
If applicable, add the following below the CDDL Header, with the fields
enclosed by brackets [] replaced by your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]" */
/*
 * Main.java
 *
 * Created on September 24, 2006, 12:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package deferredresultdemo;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.ResultProgressHandle;
import org.netbeans.spi.wizard.Summary;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

/**
 *
 * @author Tim Boudreau
 */
public class DeferredResultDemoMain {
    
    /** Creates a new instance of Main */
    public DeferredResultDemoMain() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
        Wizard wiz = 
                WizardPage.createWizard(new Class[] { 
                FirstPage.class, SecondPage.class }, 
                new WRP());
        
        System.out.println("Wizard Result: " + WizardDisplayer.showWizard(wiz));
        System.exit(0);
            }
            
        });
    }
    
    private static class WRP implements WizardResultProducer {
        public Object finish(Map wizardData) throws WizardException {
            return new BackgroundResultCreator(wizardData);
        }

        public boolean cancel(Map settings) {
            boolean dialogShouldClose = JOptionPane.showConfirmDialog (null,
                "Oh, come on, you want to finish the wizard, don't you?!!",
                "Don't Leave!", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION;
            return dialogShouldClose;        
        }
    }
    
    static boolean abortable = true;
    static class BackgroundResultCreator extends DeferredWizardResult {
        public BackgroundResultCreator(Map wizardData) {
            super (abortable);
            boolean isUseBusy = ((Boolean) wizardData.get(FirstPage.USE_BUSY)).booleanValue();
            if (isUseBusy)
            {
//TODO fix                setUseBusy (true);
            }
        }
        
        private volatile boolean aborted;
        public void abort() {
            aborted = true;
        }
        
        public void start(Map wizardData, ResultProgressHandle progress) {
           //assert !EventQueue.isDispatchThread();
            
            // test bad program handling: what happens if we don't call finished or failed?
            boolean isDoNothing = ((Boolean) wizardData.get("do_nothing")).booleanValue(); 
            if (isDoNothing)
            {
                return;
            }
            
            boolean isUseBusy = ((Boolean) wizardData.get(FirstPage.USE_BUSY)).booleanValue(); 
            for (int i = 0; i < 5; i++) {
                if (aborted) {
                    return;
                }
                if (isUseBusy)
                {
                    // progress.setBusy ("Just waiting around");
                    // progress.setProgress("Waiting for done", 100,-1);
                }
                else
                {
                    progress.setProgress("Doing stuff " + (i  * 20) + '%', i, 5);
                }
                try {
                    Thread.currentThread().sleep(1000);
                    if (aborted) {
                        return;
                    }
                } catch (InterruptedException ex) {
                    
                }
            }
            if (Boolean.TRUE.equals(wizardData.get("shouldFail"))) {
                progress.failed("The operation has failed.  This is the UI that " +
                        "is displayed when DeferredWizardResult.failed() is called.",
                        false);
            } else {
                progress.finished(createSummary (wizardData));
            }
        }
        
        private Object createSummary (Map wizardData) {
            //We will just return the wizard data here.  In real life we would
            //a compute a result here
            Summary summary;
            if (Boolean.TRUE.equals(wizardData.get("custom"))) {
                Object[][] data = new Object[wizardData.size()][2];
                int ix = 0;
                for (Iterator i=wizardData.keySet().iterator(); i.hasNext();) {
                    Object key = i.next();
                    Object val = wizardData.get(key);
                    data[ix][0] = key;
                    data[ix][1] = val;
                    ix++;
                }
                TableModel mdl = new DefaultTableModel (data, 
                        new String[] { "Key", "Value"});
                summary = Summary.create(new JScrollPane(new JTable(mdl)), wizardData);
            } else if (Boolean.TRUE.equals(wizardData.get("list"))) {
                String[] s = new String[wizardData.size()];
                int ix = 0;
                for (Iterator i=wizardData.keySet().iterator(); i.hasNext();) {
                    Object key = i.next();
                    s[ix] = key + " = " + wizardData.get(key);
                    ix++;
                }
                summary = Summary.create (s, wizardData);
            } else if (Boolean.TRUE.equals(wizardData.get("plainText"))) {
                summary = Summary.create ("This is some summary text", wizardData);
            } else {
                summary = null;
            }
            return summary == null ? (Object) wizardData : summary;
        }
    }
}
