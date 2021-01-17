package com.onyshchenko.psabot.services.command.factory;

import com.onyshchenko.psabot.models.common.Command;
import com.onyshchenko.psabot.services.command.processors.AddToWishlistProcessor;
import com.onyshchenko.psabot.services.command.processors.CabinetProcessor;
import com.onyshchenko.psabot.services.command.processors.ClearWishlistProcessor;
import com.onyshchenko.psabot.services.command.processors.DefaultProcessor;
import com.onyshchenko.psabot.services.command.processors.GamesMenuProcessor;
import com.onyshchenko.psabot.services.command.processors.GetGameProcessor;
import com.onyshchenko.psabot.services.command.processors.GetWishlistProcessor;
import com.onyshchenko.psabot.services.command.processors.GreetingsProcessor;
import com.onyshchenko.psabot.services.command.processors.RegisterUserProcessor;
import com.onyshchenko.psabot.services.command.processors.SearchGameProcessor;
import com.onyshchenko.psabot.services.command.processors.SwitchNotificationsProcessor;
import com.onyshchenko.psabot.services.command.common.CommandProcessor;
import com.onyshchenko.psabot.services.command.processors.GetGameListProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CommandProcessorFactory {

    @Autowired
    private GetGameListProcessor getGamesProcessor;
    @Autowired
    private GetGameProcessor getGameProcessor;
    @Autowired
    private RegisterUserProcessor registerUserProcessor;
    @Autowired
    private GreetingsProcessor greetingsProcessor;
    @Autowired
    private AddToWishlistProcessor addToWishlistProcessor;
    @Autowired
    private GetWishlistProcessor getWishlistProcessor;
    @Autowired
    private ClearWishlistProcessor clearWishlistProcessor;
    @Autowired
    private SearchGameProcessor searchGameProcessor;
    @Autowired
    private GamesMenuProcessor gamesMenuProcessor;
    @Autowired
    private CabinetProcessor cabinetProcessor;
    @Autowired
    private SwitchNotificationsProcessor switchNotificationsProcessor;
    @Autowired
    private DefaultProcessor defaultProcessor;

    private static final EnumMap<Command, CommandProcessor> handler = new EnumMap<>(Command.class);

    @PostConstruct
    private Map<Command, CommandProcessor> getObject() {
        handler.put(Command.GETGAMES, getGamesProcessor);
        handler.put(Command.GETGAME, getGameProcessor);
        handler.put(Command.REGISTERUSER, registerUserProcessor);
        handler.put(Command.GREETINGS, greetingsProcessor);
        handler.put(Command.ADDTOWL, addToWishlistProcessor);
        handler.put(Command.GETWL, getWishlistProcessor);
        handler.put(Command.CLEARWL, clearWishlistProcessor);
        handler.put(Command.SEARCH, searchGameProcessor);
        handler.put(Command.GAMESMENU, gamesMenuProcessor);
        handler.put(Command.CABINET, cabinetProcessor);
        handler.put(Command.SWITCH, switchNotificationsProcessor);
        handler.put(Command.REGULAR_REPLY, defaultProcessor);

        return handler;
    }

    public static CommandProcessor getProcessorForExactCommand(Command commands) {
        return Optional.ofNullable(handler.get(commands))
                .orElseThrow(() -> new IllegalArgumentException("Invalid command"));
    }

}
