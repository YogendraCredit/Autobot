Feature: Verify the workflow scenarios for INDMoney

  Scenario: INDMoney OKYC Workflow
    Given Partner sends initial offer
    When INDMoney sends final offer request and final offer workflow ends successfully
    And  Partner sends Loan Agreement
    Then Post final offer workflow starts and ends
    And Green Channel workflow is triggered
    Then Partner sends withdrawal request


  Scenario: INDMoney CKYC Workflow
    Given Partner sends initial offer
    When Partner Searches and Downloads CKYC
    Then INDMoney sends final offer request and final offer workflow ends successfully
    And  Partner sends Loan Agreement
    Then Post final offer workflow starts and ends
    Then Partner sends withdrawal request
    And KSF reviews QC steps