package dreamer.card.game.gui.node;

import java.awt.*;
import java.beans.IntrospectionException;
import java.util.HashMap;

import javax.swing.*;

import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

import com.reflexit.magiccards.core.model.ICard;
import com.reflexit.magiccards.core.model.ICardGame;
import com.reflexit.magiccards.core.model.storage.db.DBException;
import com.reflexit.magiccards.core.model.storage.db.IDataBaseCardStorage;

import dreamer.card.game.core.Tool;

/**
 * Represents a ICard element within the system
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public final class ICardNode extends BeanNode
{

    private Action[] actions;
    private final String gameName;
    private final HashMap<String, String> attributes = new HashMap<>();
    private Image image = null;

    public ICardNode(ICard card, Object bean, String gameName) throws IntrospectionException {
        super(bean, null, Lookups.singleton(card));
        setDisplayName(card.getName());
        this.gameName = gameName;
        loadAttributes(card);
        //Retrieve icon in advance
        getIcon(0);
    }

    @Override
    public Image getIcon(int type) {
        if (image == null) {
          Lookup.getDefault().lookupAll(ICardGame.class).stream().filter((game)
                  -> (game.getName().equals(gameName))).forEachOrdered((game) ->
          {
            image = Tool.loadImage(new JFrame(), game.getBackCardIcon()).getImage();
          });
        }
        return image;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Node.Cookie getCookie(Class clazz) {
        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            return (Node.Cookie) ch;
        }

        return (Cookie) super.getLookup().lookup(clazz);
    }

    @Override
    public Action[] getActions(boolean popup) {
        //TODO: Actions?
        actions = new Action[]{ //            SystemAction.get(DeleteAction.class),
        //            null,
        //            new MarauroaApplicationNode.ConfigureAction(),
        //            new MarauroaApplicationNode.AddRPZoneAction(),
        //            new MarauroaApplicationNode.StartServerAction(),
        //            new MarauroaApplicationNode.StopServerAction(),
        //            new MarauroaApplicationNode.ConnectAction(),
        //            new MarauroaApplicationNode.DisconnectAction(),
        //            null,
        //            new MarauroaApplicationNode.DeleteServerAction()
        };
        return actions;
    }

    /**
     * @return the card
     */
    public ICard getCard() {
        return getLookup().lookup(ICard.class);
    }

    private void loadAttributes(final ICard card) {
        new Thread(() -> {
            try {
                attributes.putAll(Lookup.getDefault().lookup(IDataBaseCardStorage.class).getAttributesForCard(card.getName()));
            } catch (DBException ex) {
                Exceptions.printStackTrace(ex);
            }
        }, card.getName() + " attribute loader").start();
    }

    public String getAttribute(String attr) {
        return attributes.get(attr);
    }
}
