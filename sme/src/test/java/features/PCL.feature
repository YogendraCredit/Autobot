Feature: Verify the workflow scenarios for PCL

  Scenario: PCL Workflow
    When Partner sends PCL Appform
    Then appform status should be "15"
    And PCL Partner sends withdrawal request
    And Partner checks withdrawal confirmation
    And KSF sends "Loan is disbursed" with status "1000" callback to partners