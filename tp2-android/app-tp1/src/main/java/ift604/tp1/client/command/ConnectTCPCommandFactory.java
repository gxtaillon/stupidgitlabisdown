package ift604.tp1.client.command;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.TokenGroup;
import ift604.tp1.client.State;

/**
 * Created by taig1501 on 15-10-14.
 */
public class ConnectTCPCommandFactory implements CommandFactory {
    State state;

    public ConnectTCPCommandFactory(State state) {
        this.state = state;
    }

    @Override
    public Maybe<Command> make(TokenGroup group) {
        if (group.getGroup().length == 2) {
            try {
                return Maybe.<Command>Just(
                        new ConnectTCPCommand(state,
                                Integer.parseInt(group.getGroup()[1].getName()),
                                group.getGroup()[0].getName()),
                        "made connect command");
            } catch (NumberFormatException e) {
                return Maybe.Nothing(ExceptionExtension.stringnify(e));
            }
        } else {
            return Maybe.Nothing("connect command expected first argument to be host and second to be port");
        }
    }
}
