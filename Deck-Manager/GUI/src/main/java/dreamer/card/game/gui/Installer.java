package dreamer.card.game.gui;

import com.reflexit.magiccards.core.model.storage.db.DataBaseStateListener;
import dreamer.card.game.gui.component.cardviewer.CardViewerTopComponent;
import dreamer.card.game.gui.component.game.GameTopComponent;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.windows.WindowSystemEvent;
import org.openide.windows.WindowSystemListener;

@ServiceProvider(service = DataBaseStateListener.class)
public class Installer extends ModuleInstall implements
        WindowSystemListener, DataBaseStateListener {

    @Override
    public void restored() {
        WindowManager.getDefault().addWindowSystemListener(this);
    }

    @Override
    public void beforeLoad(WindowSystemEvent event) {
        //Start in game view
        WindowManager.getDefault().setRole("game_view");
        WindowManager.getDefault().removeWindowSystemListener(this);
    }

    @Override
    public void afterLoad(WindowSystemEvent event) {
    }

    @Override
    public void beforeSave(WindowSystemEvent event) {
    }

    @Override
    public void afterSave(WindowSystemEvent event) {
    }

    @Override
    public void initialized() {
        WindowManager.getDefault().invokeWhenUIReady(() -> {
            try {
                openComponent("CardViewerTopComponent", 
                        CardViewerTopComponent.class);
                openComponent("GameTopComponent", GameTopComponent.class);
            } catch (InstantiationException | IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
    }

    private void openComponent(String id, Class<? extends TopComponent> tc)
            throws InstantiationException, IllegalAccessException {
        TopComponent gameTC
                = WindowManager.getDefault()
                .findTopComponent(id);
        if (gameTC == null) {
            gameTC = tc.newInstance();
        }
        gameTC.open();
        gameTC.requestActive();
    }
}
