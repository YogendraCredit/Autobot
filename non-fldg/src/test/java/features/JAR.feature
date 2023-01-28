Feature: Verify the workflow scenarios for JAR

  Scenario: JAR OKYC Workflow
    Given Partner sends initial offer
    When JAR sends final offer request and final offer workflow ends successfully
    And  Partner sends Loan Agreement
    Then Post final offer workflow starts and ends
    And Green Channel workflow is triggered
    Then Partner sends withdrawal request