package dreamer.card.game.gui.node.factory;

import com.reflexit.magiccards.core.model.ICardGame;
import dreamer.card.game.gui.node.ICardGameNode;
import dreamer.card.game.gui.node.actions.Reloadable;
import java.beans.IntrospectionException;
import java.util.ArrayList;
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
    private ArrayList<ICardGame> games = new ArrayList<ICardGame>();
    /**
     * The lookup for Lookup.Provider
     */
    private Lookup lookup;
    /**
     * The InstanceContent that keeps this entity's abilities
     */
    private InstanceContent instanceContent;

    public IGameChildFactory() {
        // Create an InstanceContent to hold abilities...
        instanceContent = new InstanceContent();
        // Create an AbstractLookup to expose InstanceContent contents...
        lookup = new AbstractLookup(instanceContent);
        // Add a "Reloadable" ability to this entity
        instanceContent.add(new Reloadable() {
            @Override
            public void reload() throws Exception {
                for (ICardGame game : Lookup.getDefault().lookupAll(ICardGame.class)) {
                    if (!games.contains(game)) {
                        games.add(game);
                    }
                }
            }
        });
    }

    public void refresh() {
        refresh(true);
    }

    @Override
    protected boolean createKeys(List<ICardGame> toPopulate) {
        // The query node is reloadable, isn't it? Then just
        // get this ability from the lookup ...
        Reloadable r = getLookup().lookup(Reloadable.class);
        // ... and  use the ability
        int size = games.size();
        if (r != null) {
            try {
                r.reload();
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        toPopulate.addAll(games);
        return size < games.size();
    }

    @Override
    protected Node createNodeForKey(ICardGame game) {
        try {
            return new ICardGameNode(game, new ICardSetChildFactory(game));
        } catch (IntrospectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
