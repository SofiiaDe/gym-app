package com.xstack.gymapp.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.UserService;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCreateAndDeleteStepsDef {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;
  List<User> users;
  List<User> createdUsers = new ArrayList<>();

  @Given("I create an user with firstname {string} and last name {string} and store to database")
  public void iCreateAnUserNamedAndStoreToDatabase(String firstName, String lastName) {
    User user = User.builder()
        .firstName(firstName)
        .lastName(lastName)
        .username(userService.generateUsername(firstName, lastName))
        .password("hashed_password")
        .isActive(true)
        .build();
    User newUser = userRepository.save(user);
    createdUsers.add(newUser);
  }

  @ParameterType(value = "true|True|TRUE|false|False|FALSE")
  public Boolean booleanValue(String value) {
    return Boolean.valueOf(value);
  }

  @When("I search for that user by the isActive {booleanValue}")
  public void iSearchForThatUserByTheName(Boolean value) {
    users = userRepository.findAllByIsActive(value);
  }

  @Then("I should find at least one result")
  public void iShouldFindAtLeastOneResult() {
    assertTrue(users.size() > 0);
  }

  @Given("I have created users which I want to delete from database")
  public void deleteCreatedUsers() {
//    userRepository.deleteAllById(createdUsers.stream().map(User::getId).toList());
  }

  @When("I delete user from list of created users once it is deleted from DB")
  public void iSearchForThatUserByTheName() {
    createdUsers.forEach(user -> {
      userRepository.deleteById(user.getId());
      createdUsers.remove(user);
    });
  }

  @Then("I should have {} users in the list of created users")
  public void iShouldHaveUsers(int userCount) {
    assertEquals(userCount, createdUsers.size());
  }

}
