package dreamer.card.game.gui.node.factory;

import com.reflexit.magiccards.core.model.ICardGame;
import dreamer.card.game.gui.node.ICardGameNode;
import dreamer.card.game.gui.node.actions.Reloadable;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class IGameChildFactory extends ChildFactory<ICardGame> implements Lookup.Provider {

    private static final Logger LOG = Logger.getLogger(IGameChildFactory.class.getName());
    private ArrayList<ICardGame> games = new ArrayList<>();
    /**
     * The lookup for Lookup.Provider
     */
    private final Lookup lookup;
    /**
     * The InstanceContent that keeps this entity's abilities
     */
    private final InstanceContent instanceContent;

    public IGameChildFactory(final ICardGame game) {
        // Create an InstanceContent to hold abilities...
        instanceContent = new InstanceContent();
        // Create an AbstractLookup to expose InstanceContent contents...
        lookup = new AbstractLookup(instanceContent);
        // Add a "Reloadable" ability to this entity
        instanceContent.add((Reloadable) () -> {
            if (game == null) {
                LOG.log(Level.INFO, "Games found: {0}", games.size());
                for (ICardGame game1 : Lookup.getDefault().lookupAll(ICardGame.class)) {
                    if (!games.contains(game1)) {
                        games.add(game1);
                    }
                }
            } else {
                games.add(game);
            }
            Collections.sort(games, (ICardGame o1, ICardGame o2) -> o1.getName().compareTo(o2.getName()));
        });
    }

    public void refresh() {
        refresh(false);
    }

    @Override
    protected boolean createKeys(List<ICardGame> toPopulate) {
        // The query node is reloadable, isn't it? Then just
        // get this ability from the lookup ...
        Reloadable r = getLookup().lookup(Reloadable.class);
        // ... and  use the ability
        if (r != null) {
            try {
                r.reload();
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        toPopulate.addAll(games);
        return true;
    }

    @Override
    protected Node createNodeForKey(ICardGame game) {
        Node result = null;
        try {
            result = new ICardGameNode(game, new ICardSetChildFactory(game));
        } catch (IntrospectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
