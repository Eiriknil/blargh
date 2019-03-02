Feature: Rumours creation and propagation

  Background:
    Given that there is a Place
    
  Scenario: Time passes and Events might be created  
    When time passes
    Then there is a chance that an Event is created depending on the size of the Place

  Scenario: An Event spawns a Rumour
    When an Event is created
    Then a Rumour might be created

  Scenario: A Rumour propagates to a different Place
    And there is a Route to a different Place
    When time passes
    Then a Rumour propagates to a Place along a Route
    
  Scenario: A Rumour arrives at a Place and might expire or establish itself
    When a Rumour arrives
    Then the Rumour might expire or spawn a local Rumour
    
  Scenario: A Rumour expires after time passes
    When time passes
    Then the Rumour might expire
