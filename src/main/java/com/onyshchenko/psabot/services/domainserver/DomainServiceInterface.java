package com.onyshchenko.psabot.services.domainserver;

import com.onyshchenko.psabot.models.user.User;
import org.springframework.stereotype.Service;

@Service
public interface DomainServiceInterface {

    String registerUser(User user);

    String executeByUrl(String urlName, String username);

    String addToWishList(String string, String str);

    String deleteFromWishList(String string, String str);

    void getTokenFromService(String string);

}
