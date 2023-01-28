Feature: Verify the workflow scenarios for MoneyView(MVL)

  @Regression @GreenChannel
  Scenario: Green channel workflow for MVL
    Given partner sends MVL Appform
    When KSF starts and ends Green channel workflow
    Then KSF sends "Loan application has been pre approved." with status "09" callback to partners
    And appform status should be "15"
    Then KSF sends "Loan application has been approved." with status "15" callback to partners
    When Partner sends Disbursal request for "MVL"
    Then We process disbursal from Razor-pay
    And KSF sends "Loan is disbursed" with status "1000" callback to partners
