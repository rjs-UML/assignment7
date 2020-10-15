package edu.rseymour.advancedjava.services;

import edu.rseymour.advancedjava.model.User;

/**
 * Describes an API for adding a Person and a list of the stocks
 * they are interested in.
 */
public interface UserService {

    /**
     * Add a user to the system.
     *
     * @param person the person to add.
     * @throws DuplicateUserNameException if the user name is not unique.
     * @throws UserServiceException        if there was a general problem with the service.
     */
    public void addPerson(User person) throws  DuplicateUserNameException,UserServiceException;

    /**
     * Each person can have 0 or more stocks associated with them. This methods adds that association.
     *
     * @param symbol   the symbol to add
     * @param user the user name to add the symbol to
     * @throws UnknownStockSymbolException if the stock symbol does not exist in the supported list
     *                                     of symbols.
     * @throws UnknownUserException is the specified user cannot be found.
     * @throws UserServiceException        if there was a general problem with the service.
     */
    public void associateStockWithPerson(String symbol, User user)
            throws UnknownStockSymbolException,UnknownUserException, UserServiceException;


}
