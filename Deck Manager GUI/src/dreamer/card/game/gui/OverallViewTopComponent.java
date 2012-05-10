package dreamer.card.game.gui;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//dreamer.card.game.gui//OverallView//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "OverallViewTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "properties", openAtStartup = false, roles = "game_view")
@ActionID(category = "Window", id = "dreamer.card.game.gui.OverallViewTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_OverallViewAction",
preferredID = "OverallViewTopComponent")
@Messages({
    "CTL_OverallViewAction=OverallView",
    "CTL_OverallViewTopComponent=OverallView Window",
    "HINT_OverallViewTopComponent=This is a OverallView window"
})
public final class OverallViewTopComponent extends TopComponent implements ExplorerManager.Provider {

    private final ExplorerManager mgr = new ExplorerManager();

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    public OverallViewTopComponent() {
        initComponents();
        setName(Bundle.CTL_OverallViewTopComponent());
        setToolTipText(Bundle.HINT_OverallViewTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        mgr.setRootContext(new AbstractNode(Children.create(new IGameChildFactory(), true)));
        associateLookup(ExplorerUtils.createLookup(getExplorerManager(), getActionMap()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gamePane = new OutlineView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gamePane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gamePane, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane gamePane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
