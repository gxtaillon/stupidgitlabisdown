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
public class ListenUDPCommandFactory implements CommandFactory {
    State state;

    public ListenUDPCommandFactory(State state) {
        this.state = state;
    }

    @Override
    public Maybe<Command> make(TokenGroup group) {
        if (group.getGroup().length == 0) {
            return Maybe.<Command>Just(
                    new ListenUDPCommand(state),
                    "made ListenUDP command");
        } else {
            return Maybe.Nothing("ListenUDP command expected no argument");
        }
    }
}
