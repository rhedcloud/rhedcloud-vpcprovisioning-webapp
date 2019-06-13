package edu.emory.oit.vpcprovisioning.shared;

public class AWSServicesJsonHelper {
	StringBuffer servicesJson = new StringBuffer();
	public AWSServicesJsonHelper() {
		servicesJson = new StringBuffer();
		servicesJson.append("{\"services\": [\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Comprehend\",\n" + 
				"        \"code\": \"comprehend\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (MariaDB)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-mariadb\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain Blacklisting\",\n" + 
				"                \"code\": \"domain-blacklisting\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain Whitelisting\",\n" + 
				"                \"code\": \"domain-whitelisting\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Certificate Revocation\",\n" + 
				"                \"code\": \"certificate-revocation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Validation Issue\",\n" + 
				"                \"code\": \"validation-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Certificate Manager\",\n" + 
				"        \"code\": \"amazon-acm-service\",\n" + 
				"        \"status\": \"allowed\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"OpsWorks for Chef Automate\",\n" + 
				"        \"code\": \"amazon-opsworks-chef-automate\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Alarms\",\n" + 
				"                \"code\": \"alarms\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Custom Metrics\",\n" + 
				"                \"code\": \"custom-metrics\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Events\",\n" + 
				"                \"code\": \"event\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Logs\",\n" + 
				"                \"code\": \"logs\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dashboard\",\n" + 
				"                \"code\": \"dashboard\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudWatch\",\n" + 
				"        \"code\": \"amazon-cloudwatch\",\n" + 
				"        \"status\": \"allowed\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Specific\",\n" + 
				"                \"code\": \"console-specific\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Macie\",\n" + 
				"        \"code\": \"service-macie\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Update Payment Method\",\n" + 
				"                \"code\": \"update-payment-method\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Marketplace\",\n" + 
				"                \"code\": \"marketplace-billing\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other Billing Questions\",\n" + 
				"                \"code\": \"other-billing-questions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Question About My Bill\",\n" + 
				"                \"code\": \"question\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Invoices and Reports\",\n" + 
				"                \"code\": \"invoices-and-reports\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VAT\",\n" + 
				"                \"code\": \"vat\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dispute a Charge\",\n" + 
				"                \"code\": \"dispute-charge\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Payment Issue\",\n" + 
				"                \"code\": \"payment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Free Tier\",\n" + 
				"                \"code\": \"free-tier\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Credits & Promotions\",\n" + 
				"                \"code\": \"credits-and-promotions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Consolidated Billing\",\n" + 
				"                \"code\": \"consolidated-billing\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"US Sales Tax\",\n" + 
				"                \"code\": \"us-sales-tax\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Charge Inquiry\",\n" + 
				"                \"code\": \"charge-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Reserved Instances\",\n" + 
				"                \"code\": \"reserved-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DevPay Inquiry\",\n" + 
				"                \"code\": \"devpay-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Update Billing Details\",\n" + 
				"                \"code\": \"update-billing-details\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Account Reinstatement\",\n" + 
				"                \"code\": \"account-reinstatement\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"US Sales Tax Exemption\",\n" + 
				"                \"code\": \"us-sales-tax-exemption\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain name registration issue\",\n" + 
				"                \"code\": \"domain-name-registration-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Billing\",\n" + 
				"        \"code\": \"billing\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (PostgreSQL)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-postgresql\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Plugins\",\n" + 
				"                \"code\": \"plugins\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Service Issue\",\n" + 
				"                \"code\": \"service-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"api-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device Issue\",\n" + 
				"                \"code\": \"device-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Device Farm\",\n" + 
				"        \"code\": \"device-farm\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pipeline Creation\",\n" + 
				"                \"code\": \"pipeline-creation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Stalled Pipeline\",\n" + 
				"                \"code\": \"stalled-pipeline\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Data Pipeline\",\n" + 
				"        \"code\": \"aws-data-pipeline\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"S3 Adapter\",\n" + 
				"                \"code\": \"s3-adapter\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Inquiry\",\n" + 
				"                \"code\": \"job-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Shipping Status\",\n" + 
				"                \"code\": \"shipping-status\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Manifest and Unlock Codes\",\n" + 
				"                \"code\": \"manifest-unlock-codes\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Snowball command line tool\",\n" + 
				"                \"code\": \"snowball-cli\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Data Inconsistency\",\n" + 
				"                \"code\": \"data-inconsistency\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Snowball Edge Services\",\n" + 
				"                \"code\": \"snowball-edge-services\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Requests\",\n" + 
				"                \"code\": \"feature-requests\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Slow Data Transfer\",\n" + 
				"                \"code\": \"slow-data-transfer\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Snowball device issue\",\n" + 
				"                \"code\": \"snowball-device-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity Issue\",\n" + 
				"                \"code\": \"connectivity-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Snowball\",\n" + 
				"        \"code\": \"aws-import-export-snowball\",\n" + 
				"        \"status\": \"allowed\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Machine Learning\",\n" + 
				"                \"code\": \"machine-learning\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Certificates\",\n" + 
				"                \"code\": \"certificates\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Greengrass Core\",\n" + 
				"                \"code\": \"greengrass-core\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device SDK\",\n" + 
				"                \"code\": \"device-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Group Management/Deployment\",\n" + 
				"                \"code\": \"group-management-deployment\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"api-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Greengrass\",\n" + 
				"        \"code\": \"amazon-greengrass\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Marketplace Buyer Request\",\n" + 
				"                \"code\": \"marketplace-buyer-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Marketplace Seller Request\",\n" + 
				"                \"code\": \"marketplace-seller-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Product and Support Problems\",\n" + 
				"                \"code\": \"support-problems\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Marketplace Seller Inquiry\",\n" + 
				"                \"code\": \"general-marketplace-seller-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Refund Request\",\n" + 
				"                \"code\": \"refund-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Software Launch Issue\",\n" + 
				"                \"code\": \"software-launch-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Marketplace Website Issue\",\n" + 
				"                \"code\": \"marketplace-website-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Marketplace\",\n" + 
				"        \"code\": \"amazon-marketplace\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Mobile Analytics \",\n" + 
				"        \"code\": \"mobile-analytics\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"NAT Gateway\",\n" + 
				"                \"code\": \"nat-gateway\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPC Peering\",\n" + 
				"                \"code\": \"vpc-peering\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPN Solutions\",\n" + 
				"                \"code\": \"vpn-solutions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connection Issue\",\n" + 
				"                \"code\": \"connection-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPC Endpoints\",\n" + 
				"                \"code\": \"vpc-endpoints\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DNS/DHCP Issues\",\n" + 
				"                \"code\": \"dns-dhcp-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Virtual Private Cloud (VPC)\",\n" + 
				"        \"code\": \"amazon-virtual-private-cloud\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Image Builder\",\n" + 
				"                \"code\": \"image-builder\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Management Console and SDK\",\n" + 
				"                \"code\": \"management-console-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Streaming Issue\",\n" + 
				"                \"code\": \"application-streaming-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"AppStream 2.0\",\n" + 
				"        \"code\": \"amazon-appstream2\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Artifact\",\n" + 
				"        \"code\": \"amazon-artifact\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Detection/Alert Guidance\",\n" + 
				"                \"code\": \"detection-alert-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"GuardDuty\",\n" + 
				"        \"code\": \"guardduty\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Credit Inquiry\",\n" + 
				"                \"code\": \"credit-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Account Access Issues\",\n" + 
				"                \"code\": \"account-access-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Inquiry\",\n" + 
				"                \"code\": \"application-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Educate Info\",\n" + 
				"                \"code\": \"general-info\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Veteran Inquiry\",\n" + 
				"                \"code\": \"veteran-inquiry\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Educate\",\n" + 
				"        \"code\": \"educate\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Resource Management Issues\",\n" + 
				"                \"code\": \"resource-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Deployment Issue\",\n" + 
				"                \"code\": \"deployment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Instance Issues\",\n" + 
				"                \"code\": \"instance-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Agent Issues\",\n" + 
				"                \"code\": \"agent-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Cookbook Guidance\",\n" + 
				"                \"code\": \"cookbook-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Layer Issues\",\n" + 
				"                \"code\": \"layer-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"UI/Console Issues\",\n" + 
				"                \"code\": \"console-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Stack Issues\",\n" + 
				"                \"code\": \"stack-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Issues\",\n" + 
				"                \"code\": \"application-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"OpsWorks Stacks\",\n" + 
				"        \"code\": \"aws-opsworks\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance, Framework\",\n" + 
				"                \"code\": \"general-guidance-framework\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance, Custom Algorithm\",\n" + 
				"                \"code\": \"general-guidance-custom-algorithm\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Model / Artifact Issue\",\n" + 
				"                \"code\": \"model-artifact-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Notebook Issue\",\n" + 
				"                \"code\": \"notebook-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance, Hosted Algorithms\",\n" + 
				"                \"code\": \"general-guidance-hosted-algorithms\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Issue\",\n" + 
				"                \"code\": \"job-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"SageMaker\",\n" + 
				"        \"code\": \"sagemaker\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issues\",\n" + 
				"                \"code\": \"console-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CodeStar\",\n" + 
				"        \"code\": \"code-star\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Inbound To AWS\",\n" + 
				"                \"code\": \"inbound-to-aws\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Report\",\n" + 
				"                \"code\": \"report\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Abuse Complaint\",\n" + 
				"                \"code\": \"abuse-complaint\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Distributed Denial of Service (DDoS)\",\n" + 
				"        \"code\": \"distributed-denial-of-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"MapR\",\n" + 
				"                \"code\": \"memcached-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"ElastiCache\",\n" + 
				"        \"code\": \"amazon-elasticache\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Specific\",\n" + 
				"                \"code\": \"console-specific\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Migration Hub\",\n" + 
				"        \"code\": \"migration-hub\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (SQL Server)\",\n" + 
				"        \"code\": \"aws-relational-database-service-sql-server\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Spot Fleet Scaling\",\n" + 
				"                \"code\": \"ec2-spot-fleet-scaling\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Command Line Tools\",\n" + 
				"                \"code\": \"command-line-tools\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Scaling Policy Guidance\",\n" + 
				"                \"code\": \"scaling-policy-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Auto Scaling\",\n" + 
				"        \"code\": \"auto-scaling\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions and Access Control\",\n" + 
				"                \"code\": \"permissions-and-access-control\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Ad Server Issue\",\n" + 
				"                \"code\": \"ad-server-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Configuration\",\n" + 
				"                \"code\": \"configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Origin\",\n" + 
				"                \"code\": \"origin\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Reporting\",\n" + 
				"                \"code\": \"reporting\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MediaTailor\",\n" + 
				"        \"code\": \"mediatailor\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Lightsail\",\n" + 
				"        \"code\": \"amazon-lightsail\",\n" + 
				"        \"status\": \"blocked\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Channel Output\",\n" + 
				"                \"code\": \"channel-output\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Channel Input\",\n" + 
				"                \"code\": \"channel-input\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions and Access Control\",\n" + 
				"                \"code\": \"permissions-and-access-control\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Channel/Endpoint\",\n" + 
				"                \"code\": \"channel-endpoint\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Encryption/DRM\",\n" + 
				"                \"code\": \"encryption-drm\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MediaPackage\",\n" + 
				"        \"code\": \"mediapackage\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Deployment Issues\",\n" + 
				"                \"code\": \"deployment-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions\",\n" + 
				"                \"code\": \"permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"User Assignments\",\n" + 
				"                \"code\": \"user-assignments\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Portfolios\",\n" + 
				"                \"code\": \"portfolios\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"End User Console\",\n" + 
				"                \"code\": \"end-user-console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Products\",\n" + 
				"                \"code\": \"products\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Service Catalog\",\n" + 
				"        \"code\": \"service-catalog\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"AppStream\",\n" + 
				"        \"code\": \"amazon-appstream\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Glue\",\n" + 
				"        \"code\": \"aws-glue\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SMS Connector\",\n" + 
				"                \"code\": \"sms-connector\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Replication Job\",\n" + 
				"                \"code\": \"replication-job\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Server Migration Service (Windows)\",\n" + 
				"        \"code\": \"amazon-servermigration-windows\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"API Issue\",\n" + 
				"                \"code\": \"api-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Compute Environment: Container\",\n" + 
				"                \"code\": \"compute-environment-container\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Architectural Guidance\",\n" + 
				"                \"code\": \"architectural-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions Issue\",\n" + 
				"                \"code\": \"permissions-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Batch\",\n" + 
				"        \"code\": \"amazon-batch\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Security Issue\",\n" + 
				"                \"code\": \"security-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudHSM\",\n" + 
				"        \"code\": \"aws-cloud-hsm\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Getting Started\",\n" + 
				"                \"code\": \"getting-started\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance\",\n" + 
				"                \"code\": \"performance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Systems Manager for Microsoft SCVMM\",\n" + 
				"        \"code\": \"systems-manager-for-microsoft-scvmm\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Client Library\",\n" + 
				"                \"code\": \"client-library\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Kinesis Firehose\",\n" + 
				"        \"code\": \"amazon-kinesis-firehose\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Authentication Issue\",\n" + 
				"                \"code\": \"authentication-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Identity and Access Management (IAM) \",\n" + 
				"        \"code\": \"aws-identity-and-access-management\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Alexa Services\",\n" + 
				"        \"code\": \"alexa-services\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"HTTP 4xx/5xx Errors\",\n" + 
				"                \"code\": \"http-4xx-5xx-errors\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Cross-Origin Resource Sharing (CORS)\",\n" + 
				"                \"code\": \"cors\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Distribution Issue\",\n" + 
				"                \"code\": \"distribution-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Network Issue\",\n" + 
				"                \"code\": \"network-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda@Edge\",\n" + 
				"                \"code\": \"lambda-edge\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"ACM (Amazon Certificate Manager)\",\n" + 
				"                \"code\": \"acm\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SSL Problems\",\n" + 
				"                \"code\": \"ssl-problems\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Private Content Issues (Signed URLs/Cookies)\",\n" + 
				"                \"code\": \"private-content-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Latency/Slow Downloads\",\n" + 
				"                \"code\": \"latency-slow-downloads\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"WAF (Web Application Firewall)\",\n" + 
				"                \"code\": \"waf\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Caching Issues\",\n" + 
				"                \"code\": \"caching-issues\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudFront\",\n" + 
				"        \"code\": \"amazon-cloudfront\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Build Issues\",\n" + 
				"                \"code\": \"build-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CodeBuild\",\n" + 
				"        \"code\": \"amazon-codebuild\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Polly\",\n" + 
				"        \"code\": \"amazon-polly\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Credit Based Instances Launch Credits\",\n" + 
				"                \"code\": \"ec2-credit-based-instances-launch-credits-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elasticsearch Service\",\n" + 
				"                \"code\": \"elasticsearch-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Gateway\",\n" + 
				"                \"code\": \"api-gateway-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MediaConvert\",\n" + 
				"                \"code\": \"service-code-mediaconvert\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Snowball\",\n" + 
				"                \"code\": \"import-export-snowball-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Batch\",\n" + 
				"                \"code\": \"aws-batch\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda@Edge\",\n" + 
				"                \"code\": \"aws-lambda-edge-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MediaTailor\",\n" + 
				"                \"code\": \"service-code-mediatailor\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudFront Distributions\",\n" + 
				"                \"code\": \"cloudfront-distributions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Translate\",\n" + 
				"                \"code\": \"service-code-translate\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CodePipeline\",\n" + 
				"                \"code\": \"codepipeline-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MQ\",\n" + 
				"                \"code\": \"service-code-amazon-mq\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EBS\",\n" + 
				"                \"code\": \"ebs-volumes\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SES Sending Limits\",\n" + 
				"                \"code\": \"ses-sending-quota\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"RDS\",\n" + 
				"                \"code\": \"rds-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Data Pipeline\",\n" + 
				"                \"code\": \"data-pipeline-limit-increase\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Machine Learning\",\n" + 
				"                \"code\": \"service-code-machine-learning\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Amazon Machine Images (AMIs)\",\n" + 
				"                \"code\": \"ami-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Alexa for Business\",\n" + 
				"                \"code\": \"service-code-alexa-for-business\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connect\",\n" + 
				"                \"code\": \"connect-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"ElastiCache Nodes\",\n" + 
				"                \"code\": \"elasticache-nodes\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"GameLift\",\n" + 
				"                \"code\": \"gamelift-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"OpsWorks for Chef Automate\",\n" + 
				"                \"code\": \"opsworks-chef-automate\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lightsail\",\n" + 
				"                \"code\": \"lightsail-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Systems Manager\",\n" + 
				"                \"code\": \"service-code-ec2-systems-manager\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kinesis Video Streams\",\n" + 
				"                \"code\": \"service-code-kinesis-video-streams\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SNS\",\n" + 
				"                \"code\": \"sns-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Container Registry (ECR)\",\n" + 
				"                \"code\": \"container-registry-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kinesis Firehose\",\n" + 
				"                \"code\": \"kinesis-firehose-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SageMaker\",\n" + 
				"                \"code\": \"service-code-sagemaker\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Reserved Instance Sales\",\n" + 
				"                \"code\": \"ec2-reserved-instance-seller\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"QuickSight\",\n" + 
				"                \"code\": \"quicksight-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Polly IP Address\",\n" + 
				"                \"code\": \"polly-ip-address\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudSearch Partitions and Instances\",\n" + 
				"                \"code\": \"cloudsearch-partitions-and-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Auto Scaling (Other than EC2 ASGs)\",\n" + 
				"                \"code\": \"application-auto-scaling-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudFormation Stacks\",\n" + 
				"                \"code\": \"cloudformation-stacks\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Service Catalog\",\n" + 
				"                \"code\": \"service-catalog-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic IPs\",\n" + 
				"                \"code\": \"elastic-ips\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"WorkSpaces\",\n" + 
				"                \"code\": \"workspaces-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SimpleDB Domains\",\n" + 
				"                \"code\": \"simpledb-domains\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Simple Storage Service (S3)\",\n" + 
				"                \"code\": \"simple-storage-service-limit\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Total DAX Nodes\",\n" + 
				"                \"code\": \"dax-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Dedicated Hosts\",\n" + 
				"                \"code\": \"ec2-dedicated-hosts-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Polly Account\",\n" + 
				"                \"code\": \"polly-account\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AppStream 2.0\",\n" + 
				"                \"code\": \"appstream2\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MediaLive\",\n" + 
				"                \"code\": \"service-code-medialive\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Certificate Manager\",\n" + 
				"                \"code\": \"acm-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Reserved Instance Purchase Instances\",\n" + 
				"                \"code\": \"ec2-reserved-purchase-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Step Functions\",\n" + 
				"                \"code\": \"step-functions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pinpoint\",\n" + 
				"                \"code\": \"pinpoint-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Athena\",\n" + 
				"                \"code\": \"athena-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Resource Groups\",\n" + 
				"                \"code\": \"service-code-resource-groups\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AppStream\",\n" + 
				"                \"code\": \"appstream-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Workspaces Application Manager\",\n" + 
				"                \"code\": \"workspaces-application-manager\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Simple Workflow Service (SWF)\",\n" + 
				"                \"code\": \"swf-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Config Service\",\n" + 
				"                \"code\": \"config-service-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Glacier\",\n" + 
				"                \"code\": \"amazon-glacier\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Directory Service\",\n" + 
				"                \"code\": \"directory-service-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Migration Service\",\n" + 
				"                \"code\": \"dms-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Cognito\",\n" + 
				"                \"code\": \"amazon-congnito-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"IoT\",\n" + 
				"                \"code\": \"service-code-iot\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Polly Operation\",\n" + 
				"                \"code\": \"polly-operation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Route 53\",\n" + 
				"                \"code\": \"route-53-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic Beanstalk\",\n" + 
				"                \"code\": \"elastic-beanstalk-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudHSM\",\n" + 
				"                \"code\": \"cloudhsm-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudWatch Logs\",\n" + 
				"                \"code\": \"cloudwatch-logs-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"OpsWorks Stacks\",\n" + 
				"                \"code\": \"opsworks-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudWatch\",\n" + 
				"                \"code\": \"cloudwatch-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Glue\",\n" + 
				"                \"code\": \"service-code-glue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SES Dedicated IP\",\n" + 
				"                \"code\": \"ses-dedicated-ip\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MediaPackage\",\n" + 
				"                \"code\": \"service-code-mediapackage\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda\",\n" + 
				"                \"code\": \"aws-lambda-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"IAM Groups and Users\",\n" + 
				"                \"code\": \"iam-groups-and-users\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Auto Scaling\",\n" + 
				"                \"code\": \"autoscaling-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Comprehend\",\n" + 
				"                \"code\": \"service-code-comprehend\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudWatch Events\",\n" + 
				"                \"code\": \"cloudwatch-events-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Inspector\",\n" + 
				"                \"code\": \"amazon-inspector-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Resources Enabled for Shield Advanced Protection\",\n" + 
				"                \"code\": \"shield-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic GPUs\",\n" + 
				"                \"code\": \"service-code-elastic-gpu\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SNS Text Messaging\",\n" + 
				"                \"code\": \"service-code-sns-text-messaging\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SES Production Access\",\n" + 
				"                \"code\": \"ses-production-access\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"WAF\",\n" + 
				"                \"code\": \"waf-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Discovery\",\n" + 
				"                \"code\": \"application-discovery-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kinesis Streams\",\n" + 
				"                \"code\": \"kinesis-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Consolidated Billing\",\n" + 
				"                \"code\": \"consolidated-billing-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MediaStore\",\n" + 
				"                \"code\": \"service-code-mediastore\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Container Services (ECS)\",\n" + 
				"                \"code\": \"container-services-limit\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CodeDeploy\",\n" + 
				"                \"code\": \"codedeploy-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Gateway Management\",\n" + 
				"                \"code\": \"api-gateway-management\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pinpoint Email\",\n" + 
				"                \"code\": \"service-code-pinpoint-email\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Organizations\",\n" + 
				"                \"code\": \"aws-organizations\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pinpoint SMS\",\n" + 
				"                \"code\": \"service-code-pinpoint-sms\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Redshift\",\n" + 
				"                \"code\": \"redshift-limit-increases\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic Load Balancers\",\n" + 
				"                \"code\": \"elastic-load-balancers\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DynamoDB\",\n" + 
				"                \"code\": \"dynamodb-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Rekognition\",\n" + 
				"                \"code\": \"rekognition\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CodeBuild\",\n" + 
				"                \"code\": \"codebuild-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPC\",\n" + 
				"                \"code\": \"vpc-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"IoT Device Management\",\n" + 
				"                \"code\": \"service-code-iot-device-management\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic Transcoder\",\n" + 
				"                \"code\": \"elastic-transcoder-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elastic File System (EFS)\",\n" + 
				"                \"code\": \"elastic-file-system-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Direct Connect\",\n" + 
				"                \"code\": \"direct-connect-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Instances\",\n" + 
				"                \"code\": \"ec2-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"WorkMail\",\n" + 
				"                \"code\": \"workmail-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Key Management Service\",\n" + 
				"                \"code\": \"aws-key-management-service-limits\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kinesis Analytics\",\n" + 
				"                \"code\": \"kinesis-analytics-limits\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Service Limit Increase\",\n" + 
				"        \"code\": \"service-limit-increase\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Development\",\n" + 
				"                \"code\": \"development\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Operational - Networking\",\n" + 
				"                \"code\": \"operational-networking\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Notification Issue\",\n" + 
				"                \"code\": \"notification-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Operational - Topic Specific\",\n" + 
				"                \"code\": \"operational-topic\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Operational - Subscription Specific\",\n" + 
				"                \"code\": \"operational-subscription\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Simple Notification Service (SNS)\",\n" + 
				"        \"code\": \"amazon-simple-notification-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"FreeRTOS Kernel\",\n" + 
				"                \"code\": \"freertos-kernel\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"FreeRTOS Device Libraries\",\n" + 
				"                \"code\": \"freertos-device-libraries\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK/CLI\",\n" + 
				"                \"code\": \"sdk-cli\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device Qualification\",\n" + 
				"                \"code\": \"device-qualification\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device Drivers/Board Support Packages\",\n" + 
				"                \"code\": \"device-drivers-board-support-packages\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature/Documentation Request\",\n" + 
				"                \"code\": \"feature-documentation-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"FreeRTOS\",\n" + 
				"        \"code\": \"freertos\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"CloudWatch Events\",\n" + 
				"                \"code\": \"cloudwatch-events\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions and Access Control\",\n" + 
				"                \"code\": \"permissions-and-access-control\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Channel in Degraded state\",\n" + 
				"                \"code\": \"channel-in-degraded-state\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Partial Output\",\n" + 
				"                \"code\": \"partial-output\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Unable to stop/start/delete Channel\",\n" + 
				"                \"code\": \"unable-to-stop-start-delete-channel\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MediaLive\",\n" + 
				"        \"code\": \"medialive\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Inbound Attack to AWS\",\n" + 
				"                \"code\": \"inbound-attack-to-aws\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Shield\",\n" + 
				"        \"code\": \"aws-shield\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Deployment Issue\",\n" + 
				"                \"code\": \"deployment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CodeDeploy\",\n" + 
				"        \"code\": \"codedeploy\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Issues: Agentless Connector\",\n" + 
				"                \"code\": \"agentless-connector\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Issues: Agent\",\n" + 
				"                \"code\": \"issues-agent\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Application Discovery Service (Windows)\",\n" + 
				"        \"code\": \"aws-application-discovery-windows\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Object Issue\",\n" + 
				"                \"code\": \"object-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Analytics and Metrics\",\n" + 
				"                \"code\": \"analytics-and-metrics\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Network Issue\",\n" + 
				"                \"code\": \"network-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions and Access Control\",\n" + 
				"                \"code\": \"permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Static Website Hosting\",\n" + 
				"                \"code\": \"static-website-hosting\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Notifications and Logging\",\n" + 
				"                \"code\": \"notifications-and-logging\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Consistency and Lifecycle Rules\",\n" + 
				"                \"code\": \"lifecycle-rules\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Transfer Acceleration Issue\",\n" + 
				"                \"code\": \"transfer-acceleration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Bucket Issue\",\n" + 
				"                \"code\": \"bucket-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Simple Storage Service (S3)\",\n" + 
				"        \"code\": \"amazon-simple-storage-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Health\",\n" + 
				"        \"code\": \"aws-health\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Load Balancer Issue\",\n" + 
				"                \"code\": \"load-balancer-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Load Balancing (ELB)\",\n" + 
				"        \"code\": \"elastic-load-balancing\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DNS Issue\",\n" + 
				"                \"code\": \"dns-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain name registration issue\",\n" + 
				"                \"code\": \"domain-name-registration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Health Checks\",\n" + 
				"                \"code\": \"health-checks\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Route 53\",\n" + 
				"        \"code\": \"amazon-route53\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Web App\",\n" + 
				"                \"code\": \"web-app\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain & User Administration\",\n" + 
				"                \"code\": \"domain-and-user-administration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Desktop Client\",\n" + 
				"                \"code\": \"desktop-client\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Mobile\",\n" + 
				"                \"code\": \"mobile\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Email Issue\",\n" + 
				"                \"code\": \"email-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"WorkMail\",\n" + 
				"        \"code\": \"workmail\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK Issue\",\n" + 
				"                \"code\": \"sdk-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Command Line Tools\",\n" + 
				"                \"code\": \"command-line-tools\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda Integration\",\n" + 
				"                \"code\": \"lambda-integration\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Lex\",\n" + 
				"        \"code\": \"amazon-lex\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Issues: Agent\",\n" + 
				"                \"code\": \"issues-agent\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Issues: Agentless Connector\",\n" + 
				"                \"code\": \"issues-agentless-connector\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Application Discovery Service (Linux)\",\n" + 
				"        \"code\": \"aws-application-discovery-linux\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (Oracle)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-oracle\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Access and Permissions\",\n" + 
				"                \"code\": \"access-permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Phone Number Requests\",\n" + 
				"                \"code\": \"phone-number-requests\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Contact Center Configuration\",\n" + 
				"                \"code\": \"contact-center-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Country Whitelisting for Outbound Calls\",\n" + 
				"                \"code\": \"country-whitelisting-outbound-calls\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Phone Number Porting\",\n" + 
				"                \"code\": \"phone-number-porting\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Integration\",\n" + 
				"                \"code\": \"integration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Call Quality\",\n" + 
				"                \"code\": \"call-quality\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Contact Handling & Routing\",\n" + 
				"                \"code\": \"contact-handling-routing\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-requests\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Contact Center Metrics\",\n" + 
				"                \"code\": \"contact-center-metrics\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Connect (Contact Center)\",\n" + 
				"        \"code\": \"connect\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SSM Console\",\n" + 
				"                \"code\": \"ssm-console\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Systems Manager\",\n" + 
				"        \"code\": \"systems-manager\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Analytics\",\n" + 
				"                \"code\": \"analytics\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Campaign Management\",\n" + 
				"                \"code\": \"campaign-management\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Import Jobs\",\n" + 
				"                \"code\": \"import-jobs\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Limit Increase\",\n" + 
				"                \"code\": \"limit-increase\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"api-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Pinpoint\",\n" + 
				"        \"code\": \"amazon-pinpoint\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Run Command\",\n" + 
				"                \"code\": \"ec2-run-command\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Server Migration Service\",\n" + 
				"                \"code\": \"server-migration-service\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"FTP\",\n" + 
				"                \"code\": \"ftp\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SSM API\",\n" + 
				"                \"code\": \"ssm-api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPN Solutions\",\n" + 
				"                \"code\": \"vpn-solutions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dedicated Host - Licensing Issue\",\n" + 
				"                \"code\": \"dedicated-host-licensing-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VM Import / Export\",\n" + 
				"                \"code\": \"vm-import-export\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dedicated Host - Launch Issue\",\n" + 
				"                \"code\": \"dedicated-host-launch-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AMI Copy\",\n" + 
				"                \"code\": \"ami-copy\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SQL Server\",\n" + 
				"                \"code\": \"sql-server\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Email Services\",\n" + 
				"                \"code\": \"email-services\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"IIS\",\n" + 
				"                \"code\": \"iis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Spot Instances\",\n" + 
				"                \"code\": \"windows-spot-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Apache\",\n" + 
				"                \"code\": \"apache\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Disk Configuration\",\n" + 
				"                \"code\": \"disk-configurations\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MySQL\",\n" + 
				"                \"code\": \"mysql\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Instance Issue\",\n" + 
				"                \"code\": \"instance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SSM API Domain Join\",\n" + 
				"                \"code\": \"ssm-api-domain-join\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Systems Manager\",\n" + 
				"                \"code\": \"systems-manager\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Compute Cloud (EC2 - Windows)\",\n" + 
				"        \"code\": \"amazon-elastic-compute-cloud-windows\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SMS Connector\",\n" + 
				"                \"code\": \"sms-connector\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Replication Job\",\n" + 
				"                \"code\": \"replication-job\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Server Migration Service (Linux)\",\n" + 
				"        \"code\": \"amazon-servermigration-linux\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Rule Configuration\",\n" + 
				"                \"code\": \"rule-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Reporting\",\n" + 
				"                \"code\": \"reporting\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"WAF\",\n" + 
				"        \"code\": \"aws-web-application-firewall\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connection Issue\",\n" + 
				"                \"code\": \"connection-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Direct Connect\",\n" + 
				"        \"code\": \"aws-direct-connect\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"GameLift SDK\",\n" + 
				"                \"code\": \"gamelift-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Game Sessions\",\n" + 
				"                \"code\": \"game-sessions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Builds and Fleets\",\n" + 
				"                \"code\": \"category-builds-fleets\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Player Sessions\",\n" + 
				"                \"code\": \"player-sessions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"GameLift\",\n" + 
				"        \"code\": \"gamelift\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connection Issue\",\n" + 
				"                \"code\": \"connection-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"WorkSpaces\",\n" + 
				"        \"code\": \"amazon-workspaces\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Mobile Application Issue\",\n" + 
				"                \"code\": \"mobile-application-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Sync Application Issue\",\n" + 
				"                \"code\": \"sync-application-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Web Application Issue\",\n" + 
				"                \"code\": \"web-application-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"WorkDocs\",\n" + 
				"        \"code\": \"zocalo\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Schema Conversion Tool\",\n" + 
				"                \"code\": \"schema-conversion-tool\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Database Migration Service\",\n" + 
				"        \"code\": \"amazon-database-migration-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device Configuration\",\n" + 
				"                \"code\": \"device-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Skills Issue\",\n" + 
				"                \"code\": \"skills-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Hardware Failure\",\n" + 
				"                \"code\": \"hardware-failure\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"api-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Alexa for Business\",\n" + 
				"        \"code\": \"alexa-for-business\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Template Issue\",\n" + 
				"                \"code\": \"template-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudFormation\",\n" + 
				"        \"code\": \"aws-cloudformation\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"IoT Device Management\",\n" + 
				"        \"code\": \"iot-device-management\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Volume Issue\",\n" + 
				"                \"code\": \"volume-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Block Store (EBS)\",\n" + 
				"        \"code\": \"amazon-elastic-block-store\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [{\n" + 
				"            \"name\": \"General Account Question\",\n" + 
				"            \"code\": \"billing\"\n" + 
				"        }],\n" + 
				"        \"name\": \"Account Management\",\n" + 
				"        \"code\": \"account-management\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Encryption SDK\",\n" + 
				"                \"code\": \"encryption-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Resource Creation Errors\",\n" + 
				"                \"code\": \"resource-creation-errors\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CLI Issue\",\n" + 
				"                \"code\": \"cli-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Policy Issues\",\n" + 
				"                \"code\": \"policy-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Authorization Issue\",\n" + 
				"                \"code\": \"authorization-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Key Management Service\",\n" + 
				"        \"code\": \"key-management-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Git Issues\",\n" + 
				"                \"code\": \"git-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Repository Issues\",\n" + 
				"                \"code\": \"repository-issues\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CodeCommit\",\n" + 
				"        \"code\": \"codecommit\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Amazon WAM Client\",\n" + 
				"                \"code\": \"wam-client\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Bring Your Own Application\",\n" + 
				"                \"code\": \"bring-your-own-app\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AWS Marketplace for Desktop Apps\",\n" + 
				"                \"code\": \"marketplace-for-desktop-apps\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"WorkSpaces Application Manager\",\n" + 
				"        \"code\": \"workspaces-application-manager\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Vault Issue\",\n" + 
				"                \"code\": \"vault-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Restore from S3 Glacier Storage Class\",\n" + 
				"                \"code\": \"restore-from-s3\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK / CLI\",\n" + 
				"                \"code\": \"sdk-cli\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Glacier Select\",\n" + 
				"                \"code\": \"glacier-select\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Archive Issue\",\n" + 
				"                \"code\": \"archive-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Glacier\",\n" + 
				"        \"code\": \"amazon-glacier\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Command Line Tools\",\n" + 
				"                \"code\": \"command-line-tools\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Mechanical Turk\",\n" + 
				"        \"code\": \"amazon-mechanical-turk\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Puppet Support\",\n" + 
				"                \"code\": \"puppet-support\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"OpsWorks for Puppet Enterprise\",\n" + 
				"        \"code\": \"opsworks-puppet-enterprise\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"SDK/API Guidance\",\n" + 
				"                \"code\": \"sdk-api-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MQ\",\n" + 
				"        \"code\": \"amazon-mq\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Service Functionality\",\n" + 
				"                \"code\": \"service-functionality\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK/API Guidance\",\n" + 
				"                \"code\": \"sdk-api-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Documentation\",\n" + 
				"                \"code\": \"documentation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Configuration Guidance\",\n" + 
				"                \"code\": \"configuration-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK Bug\",\n" + 
				"                \"code\": \"sdk-bug\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Architectural Guidance/Best Practices\",\n" + 
				"                \"code\": \"architectural-guidance-best-practices\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"AppSync\",\n" + 
				"        \"code\": \"appsync\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Issues Deploying Application\",\n" + 
				"                \"code\": \"issues-deploying-application\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Access and Permissions\",\n" + 
				"                \"code\": \"access-and-permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Issues Publishing Applications\",\n" + 
				"                \"code\": \"issues-publishing-applications\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Reporting Malicious Apps\",\n" + 
				"                \"code\": \"reporting-malicious-apps\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Licensing Inquiry\",\n" + 
				"                \"code\": \"licensing-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Serverless Application Repository\",\n" + 
				"        \"code\": \"serverless-application-repository\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Device SDK/Hardware/Kits\",\n" + 
				"                \"code\": \"device-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Environment Issue\",\n" + 
				"                \"code\": \"environment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"api-sdk\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"IoT\",\n" + 
				"        \"code\": \"aws-iot\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Support API\",\n" + 
				"        \"code\": \"support-api\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Translate\",\n" + 
				"        \"code\": \"translate\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions\",\n" + 
				"                \"code\": \"real-permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Service Issue\",\n" + 
				"                \"code\": \"service-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Resource Issue\",\n" + 
				"                \"code\": \"resource-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Lambda\",\n" + 
				"        \"code\": \"aws-lambda\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Server Issues (Windows)\",\n" + 
				"                \"code\": \"server-issues-windows\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Client issues\",\n" + 
				"                \"code\": \"client-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Server Issues (Linux)\",\n" + 
				"                \"code\": \"server-issues-linux\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"DCV (Desktop Cloud Visualization)\",\n" + 
				"        \"code\": \"dcv-desktop-cloud-visualization\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"API / SDK / CLI\",\n" + 
				"                \"code\": \"api-sdk-cli\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Container Registry\",\n" + 
				"                \"code\": \"container-registry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Container Agent\",\n" + 
				"                \"code\": \"container-agent\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Container Service (ECS)\",\n" + 
				"        \"code\": \"ec2-container-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Access Issue\",\n" + 
				"                \"code\": \"access-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"File System Configuration\",\n" + 
				"                \"code\": \"file-system-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance\",\n" + 
				"                \"code\": \"performance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EFS File Sync\",\n" + 
				"                \"code\": \"efs-file-sync\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic File System\",\n" + 
				"        \"code\": \"amazon-elastic-file-system\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (MySQL)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-mysql\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Specific\",\n" + 
				"                \"code\": \"console-specific\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"DynamoDB Accelerator (DAX)\",\n" + 
				"        \"code\": \"dynamodb-accelerator\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Managing Chime Teams\",\n" + 
				"                \"code\": \"managing-chime-teams\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Chat\",\n" + 
				"                \"code\": \"chat\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"In Meeting Experience\",\n" + 
				"                \"code\": \"in-meeting-experience\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Managing Meetings\",\n" + 
				"                \"code\": \"managing-meetings\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Outlook Add-in Issue\",\n" + 
				"                \"code\": \"outlook-addin-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Scheduling Issue\",\n" + 
				"                \"code\": \"scheduling-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Chime\",\n" + 
				"        \"code\": \"chime\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Configuration Assistance\",\n" + 
				"                \"code\": \"configuration-assistance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Config Service\",\n" + 
				"        \"code\": \"config-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Executions\",\n" + 
				"                \"code\": \"executions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Activities\",\n" + 
				"                \"code\": \"activities\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Amazon States Language\",\n" + 
				"                \"code\": \"language\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions/IAM\",\n" + 
				"                \"code\": \"permissions-iam\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Step Functions\",\n" + 
				"        \"code\": \"aws-step-functions\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Single Sign-On\",\n" + 
				"        \"code\": \"single-sign-on\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Log File Delivery\",\n" + 
				"                \"code\": \"log-delivery\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Log File Notification\",\n" + 
				"                \"code\": \"log-notification\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudTrail\",\n" + 
				"        \"code\": \"cloudtrail\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Development - API Migration\",\n" + 
				"                \"code\": \"development-api-migration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issues\",\n" + 
				"                \"code\": \"console-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Queue Issue\",\n" + 
				"                \"code\": \"queue-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Requests\",\n" + 
				"                \"code\": \"feature-requests\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Simple Queue Service (SQS)\",\n" + 
				"        \"code\": \"amazon-simple-queue-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 environment creation\",\n" + 
				"                \"code\": \"ec2-environment-creation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions\",\n" + 
				"                \"code\": \"permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Environment issue\",\n" + 
				"                \"code\": \"environment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CodeStar\",\n" + 
				"                \"code\": \"codestar\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Cloud9\",\n" + 
				"        \"code\": \"cloud9\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Performance\",\n" + 
				"                \"code\": \"performance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connection Establishment\",\n" + 
				"                \"code\": \"connection-establishment\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Tunnel Stability\",\n" + 
				"                \"code\": \"tunnel-stability\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"VPN\",\n" + 
				"        \"code\": \"amazon-virtual-private-network\",\n" + 
				"        \"status\": \"blocked\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Neptune\",\n" + 
				"        \"code\": \"neptune\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Trusted Advisor\",\n" + 
				"        \"code\": \"aws-trusted-advisor\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Permissions and Security\",\n" + 
				"                \"code\": \"api-permissions-security\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Back-End Endpoint Integration\",\n" + 
				"                \"code\": \"backend-endpoint-integration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Cache\",\n" + 
				"                \"code\": \"api-cache\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Generated SDK\",\n" + 
				"                \"code\": \"generated-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Versioning\",\n" + 
				"                \"code\": \"api-versioning\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Import / Export of APIs\",\n" + 
				"                \"code\": \"import-export-api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Architectural Guidance\",\n" + 
				"                \"code\": \"architectural-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Mapping Templates\",\n" + 
				"                \"code\": \"mapping-template\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Errors and Performance Issues\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other AWS Services\",\n" + 
				"                \"code\": \"other-aws-services\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Custom Domain Names\",\n" + 
				"                \"code\": \"custom-domain-names\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API Configuration\",\n" + 
				"                \"code\": \"api-configuration\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"API Gateway\",\n" + 
				"        \"code\": \"api-gateway\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AWS Flow Framework\",\n" + 
				"                \"code\": \"aws-flow-framework\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Designing Workflows\",\n" + 
				"                \"code\": \"designing-workflows\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Simple Workflow Service (SWF)\",\n" + 
				"        \"code\": \"amazon-simple-workflow-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions (S3/IAM Roles)\",\n" + 
				"                \"code\": \"permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Transcoding Pipeline\",\n" + 
				"                \"code\": \"transcoding-pipeline\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Transcoding Presets\",\n" + 
				"                \"code\": \"transcoding-presets\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Transcoding Jobs\",\n" + 
				"                \"code\": \"transcoding-jobs\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SNS Notifications\",\n" + 
				"                \"code\": \"sns-notifications\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Format Support\",\n" + 
				"                \"code\": \"format-support\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Codec Parameters\",\n" + 
				"                \"code\": \"codec-parameters\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Transcoder\",\n" + 
				"        \"code\": \"amazon-elastic-transcoder\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Daemon Issues\",\n" + 
				"                \"code\": \"daemon-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"X Ray\",\n" + 
				"        \"code\": \"amazon-xray\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Specific\",\n" + 
				"                \"code\": \"console-specific\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Organizations\",\n" + 
				"        \"code\": \"aws-organizations\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"SDK/CLI/API Issue\",\n" + 
				"                \"code\": \"sdk-cli-api-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Object Issue\",\n" + 
				"                \"code\": \"object-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions and Access Control\",\n" + 
				"                \"code\": \"permissions-and-access-control\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Throughput/Latency Issues\",\n" + 
				"                \"code\": \"throughput-latency-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Container Issue\",\n" + 
				"                \"code\": \"container-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MediaStore\",\n" + 
				"        \"code\": \"mediastore\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kinesis Client Library\",\n" + 
				"                \"code\": \"kinesis-client-library\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Kinesis Streams\",\n" + 
				"        \"code\": \"amazon-kinesis\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SQL Syntax\",\n" + 
				"                \"code\": \"sql-syntax\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Input Schema Configuration\",\n" + 
				"                \"code\": \"input-schema-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Kinesis Analytics\",\n" + 
				"        \"code\": \"amazon-kinesis-analytics\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK / Email Applications\",\n" + 
				"                \"code\": \"sdk-email-applications\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Delivery Failure\",\n" + 
				"                \"code\": \"delivery-failure\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Domain / Email Verification\",\n" + 
				"                \"code\": \"domain-email-verification\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions / Policies\",\n" + 
				"                \"code\": \"permissions-policies\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Bounces / Complaints / Blacklists\",\n" + 
				"                \"code\": \"bounces-complaints-blacklists\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Monitoring / Notifications / Auditing\",\n" + 
				"                \"code\": \"monitoring-notifications-auditing\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Improving Email Deliverability\",\n" + 
				"                \"code\": \"improving-email-deliverability\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Inbound Email\",\n" + 
				"                \"code\": \"inbound-email\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Suspension / Probation\",\n" + 
				"                \"code\": \"suspension-probation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Production Access\",\n" + 
				"                \"code\": \"production-access\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Email Issue\",\n" + 
				"                \"code\": \"email-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Simple Email Service (SES)\",\n" + 
				"        \"code\": \"amazon-simple-email-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other / Limit Increase / Access\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions\",\n" + 
				"                \"code\": \"permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Errors and Performance\",\n" + 
				"                \"code\": \"errors-performance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Requests\",\n" + 
				"                \"code\": \"feature-requests\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Lambda@Edge\",\n" + 
				"        \"code\": \"lambda-edge\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VM Import / Export\",\n" + 
				"                \"code\": \"vm-import-export\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"vCenter Management Portal\",\n" + 
				"        \"code\": \"vcenter-management-portal\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"How Will I Be Charged?\",\n" + 
				"                \"code\": \"charges\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Reserved Instances\",\n" + 
				"                \"code\": \"reserved-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Where is my Resource?\",\n" + 
				"                \"code\": \"resource\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Using AWS & Services\",\n" + 
				"                \"code\": \"using-aws\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Free Tier\",\n" + 
				"                \"code\": \"free-tier\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Security & Compliance\",\n" + 
				"                \"code\": \"security-and-compliance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Account Structure\",\n" + 
				"                \"code\": \"account-structure\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"General Info and Getting Started\",\n" + 
				"        \"code\": \"general-info\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (Aurora - MySQL-compatible)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-aurora\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Service Issue\",\n" + 
				"                \"code\": \"service-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Project Configuration\",\n" + 
				"                \"code\": \"project-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Build Issue\",\n" + 
				"                \"code\": \"build-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Mobile Hub\",\n" + 
				"        \"code\": \"mobile-hub\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Domain Issue\",\n" + 
				"                \"code\": \"domain-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Query Issue\",\n" + 
				"                \"code\": \"query-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"SimpleDB\",\n" + 
				"        \"code\": \"amazon-simpledb\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Directory Issue\",\n" + 
				"                \"code\": \"directory-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Directory Service\",\n" + 
				"        \"code\": \"directory-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Unable to Access my Account\",\n" + 
				"                \"code\": \"unable-to-access\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Security\",\n" + 
				"                \"code\": \"security\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Close/Cancel My Account\",\n" + 
				"                \"code\": \"close-account\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other Account Issues\",\n" + 
				"                \"code\": \"other-account-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Ownership Transfer\",\n" + 
				"                \"code\": \"ownership-transfer\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Change Account Details\",\n" + 
				"                \"code\": \"change-account-details\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Activation\",\n" + 
				"                \"code\": \"activation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Compliance & Accreditations\",\n" + 
				"                \"code\": \"compliance-and-accreditation\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Account\",\n" + 
				"        \"code\": \"customer-account\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Relational Database Service (Aurora - PostgreSQL-compatible)\",\n" + 
				"        \"code\": \"amazon-relational-database-service-aurora-postgres\",\n" + 
				"        \"status\": \"allowed-with-countermeasures\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"API / SDK / CLI\",\n" + 
				"                \"code\": \"api-sdk-cli\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Custom Actions\",\n" + 
				"                \"code\": \"custom-actions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pipeline Setup\",\n" + 
				"                \"code\": \"pipeline-setup\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Building and Transitions\",\n" + 
				"                \"code\": \"building-and-transitions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CodePipeline\",\n" + 
				"        \"code\": \"codepipeline\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Manifest Creation\",\n" + 
				"                \"code\": \"manifest-creation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Inquiry\",\n" + 
				"                \"code\": \"job-inquiry\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Shipping Issue\",\n" + 
				"                \"code\": \"shipping-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Import Export (Disk)\",\n" + 
				"        \"code\": \"aws-import\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Stream Parser Library\",\n" + 
				"                \"code\": \"stream-parser-library\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Producer SDK\",\n" + 
				"                \"code\": \"producer-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Kinesis Video Streams\",\n" + 
				"        \"code\": \"kinesis-video-streams\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"AWS Resource Management\",\n" + 
				"                \"code\": \"resource-management\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DynamoDB (Database)\",\n" + 
				"                \"code\": \"dynamodb-database\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SQS (Message queuing service)\",\n" + 
				"                \"code\": \"sqs-message-queuing-service\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AWS Resource Access & Permissions\",\n" + 
				"                \"code\": \"resource-access-permissions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda (Cloud functions)\",\n" + 
				"                \"code\": \"lambda-cloud-functions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"CloudCanvas/Unspecified\",\n" + 
				"                \"code\": \"unspecified\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Team Management & Credentials\",\n" + 
				"                \"code\": \"team-management-creds\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"S3 (Storage)\",\n" + 
				"                \"code\": \"s3-storage\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SNS (Notification service)\",\n" + 
				"                \"code\": \"sns-notification-service\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Cognito (Player identity)\",\n" + 
				"                \"code\": \"cognito-player-identity\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Lumberyard\",\n" + 
				"        \"code\": \"lumberyard\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"CloudSearch \",\n" + 
				"        \"code\": \"amazon-cloudsearch\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Fargate\",\n" + 
				"        \"code\": \"fargate\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issue\",\n" + 
				"                \"code\": \"console-issue\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Inspector\",\n" + 
				"        \"code\": \"aws-inspector\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Connectivity\",\n" + 
				"                \"code\": \"connectivity\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Migration Issue\",\n" + 
				"                \"code\": \"migration-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Limit Increase\",\n" + 
				"                \"code\": \"limit-increase\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Redshift Spectrum\",\n" + 
				"                \"code\": \"redshift-spectrum\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Redshift\",\n" + 
				"        \"code\": \"amazon-redshift\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Web Application Issues\",\n" + 
				"                \"code\": \"web-application-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Datasource Issues\",\n" + 
				"                \"code\": \"datasource-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"QuickSight\",\n" + 
				"        \"code\": \"amazon-quicksight\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Query Related Issue\",\n" + 
				"                \"code\": \"query-related-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Athena\",\n" + 
				"        \"code\": \"amazon-athena\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Cloud Integration\",\n" + 
				"                \"code\": \"cloud-integration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Installation/Configuration\",\n" + 
				"                \"code\": \"installation-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Scripting/SDK\",\n" + 
				"                \"code\": \"scripting-sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Client Application Operation\",\n" + 
				"                \"code\": \"application-operation\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Licensing Issue\",\n" + 
				"                \"code\": \"licensing-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Rendering/Submission Issue\",\n" + 
				"                \"code\": \"rendering-submission-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Infrastructure Issue\",\n" + 
				"                \"code\": \"infrastructure-issues\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Deadline\",\n" + 
				"        \"code\": \"service-deadline\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Logstash\",\n" + 
				"                \"code\": \"logstash\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance\",\n" + 
				"                \"code\": \"performance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Elasticsearch\",\n" + 
				"                \"code\": \"elasticsearch\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Query\",\n" + 
				"                \"code\": \"query\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Kibana\",\n" + 
				"                \"code\": \"kibana\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elasticsearch Service\",\n" + 
				"        \"code\": \"amazon-elasticsearch-service\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Job Status Notifications (CloudWatch Events)\",\n" + 
				"                \"code\": \"job-status-notifications-cloudwatch-events\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"API/SDK Issues\",\n" + 
				"                \"code\": \"api-sdk-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Console Issues\",\n" + 
				"                \"code\": \"console-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Media Processing Issues\",\n" + 
				"                \"code\": \"media-processing-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"DRM/Encryption\",\n" + 
				"                \"code\": \"drm-encryption\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Permissions (IAM Role)\",\n" + 
				"                \"code\": \"permissions-iam-role\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Validation Issues\",\n" + 
				"                \"code\": \"job-validation-issues\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"MediaConvert\",\n" + 
				"        \"code\": \"mediaconvert\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Hive\",\n" + 
				"                \"code\": \"hive\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Flow Issue\",\n" + 
				"                \"code\": \"job-flow-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Stuck Job\",\n" + 
				"                \"code\": \"stuck-job\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MapR\",\n" + 
				"                \"code\": \"mapr\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Limit Increase\",\n" + 
				"                \"code\": \"limit-increase\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Pig\",\n" + 
				"                \"code\": \"pig\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Hadoop Configuration\",\n" + 
				"                \"code\": \"hadoop-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Failed\",\n" + 
				"                \"code\": \"job-failed\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"HBase\",\n" + 
				"                \"code\": \"hbase\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic MapReduce (EMR)\",\n" + 
				"        \"code\": \"amazon-elastic-mapreduce\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"RDS Guidance\",\n" + 
				"                \"code\": \"rds-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Infrastructure Guidance\",\n" + 
				"                \"code\": \"infrastructure-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Application Deployment Issue\",\n" + 
				"                \"code\": \"application-deployment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Environment Issue\",\n" + 
				"                \"code\": \"environment-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Beanstalk\",\n" + 
				"        \"code\": \"aws-elastic-beanstalk\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Console\",\n" + 
				"                \"code\": \"console\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"SDK Issue\",\n" + 
				"                \"code\": \"sdk\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Lambda Integration\",\n" + 
				"                \"code\": \"lambda-integration\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Cognito\",\n" + 
				"        \"code\": \"amazon-cognito\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Video Recognition Failure\",\n" + 
				"                \"code\": \"video-rekognition-failure\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Image API\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Image Recognition Failure\",\n" + 
				"                \"code\": \"image-recognition-failure\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Video API\",\n" + 
				"                \"code\": \"video-api\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Rekognition\",\n" + 
				"        \"code\": \"amazon-rekognition\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Performance Issue\",\n" + 
				"                \"code\": \"performance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Network Issue\",\n" + 
				"                \"code\": \"network-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Gateway Appliance Issue\",\n" + 
				"                \"code\": \"gateway-appliance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Storage Gateway\",\n" + 
				"        \"code\": \"aws-storage-gateway\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"");
		servicesJson.append("        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Database Issue\",\n" + 
				"                \"code\": \"database-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"DynamoDB\",\n" + 
				"        \"code\": \"amazon-dynamodb\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"SSH Issue\",\n" + 
				"                \"code\": \"ssh-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Spot Instances\",\n" + 
				"                \"code\": \"linux-spot-instances\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"EC2 Run Command\",\n" + 
				"                \"code\": \"ec2-run-command\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Server Migration Service\",\n" + 
				"                \"code\": \"server-migration-service\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Other\",\n" + 
				"                \"code\": \"other\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"FTP\",\n" + 
				"                \"code\": \"ftp\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VPN Solutions\",\n" + 
				"                \"code\": \"vpn-solutions\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dedicated Host - Licensing Issue\",\n" + 
				"                \"code\": \"dedicated-host-licensing-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"VM Import / Export\",\n" + 
				"                \"code\": \"vm-import-export\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Dedicated Host - Launch Issue\",\n" + 
				"                \"code\": \"dedicated-host-launch-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"AMI Copy\",\n" + 
				"                \"code\": \"ami-copy\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Email Services\",\n" + 
				"                \"code\": \"email-services\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Apache\",\n" + 
				"                \"code\": \"apache\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Scheduled Event\",\n" + 
				"                \"code\": \"scheduled-event\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Disk Configuration\",\n" + 
				"                \"code\": \"disk-configurations\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"MySQL\",\n" + 
				"                \"code\": \"mysql\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Instance Issue\",\n" + 
				"                \"code\": \"instance-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Systems Manager\",\n" + 
				"                \"code\": \"systems-manager\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Elastic Compute Cloud (EC2 - Linux)\",\n" + 
				"        \"code\": \"amazon-elastic-compute-cloud-linux\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"categories\": [\n" + 
				"            {\n" + 
				"                \"name\": \"Job Flow Issue\",\n" + 
				"                \"code\": \"job-flow-issue\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Machine Learning Configuration\",\n" + 
				"                \"code\": \"machine-learning-configuration\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"APIs\",\n" + 
				"                \"code\": \"apis\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Datasource - Database\",\n" + 
				"                \"code\": \"datasource-database\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Feature Request\",\n" + 
				"                \"code\": \"feature-request\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Job Failed\",\n" + 
				"                \"code\": \"job-failed\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"General Guidance\",\n" + 
				"                \"code\": \"general-guidance\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Datasource - S3\",\n" + 
				"                \"code\": \"datasource-s3\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"                \"name\": \"Machine Learning Model Quality\",\n" + 
				"                \"code\": \"model-quality\"\n" + 
				"            }\n" + 
				"        ],\n" + 
				"        \"name\": \"Machine Learning\",\n" + 
				"        \"code\": \"amazon-machine-learning\",\n" + 
				"        \"status\": \"blocked-pending-security-review\"\n" + 
				"    }\n" + 
				"]}");
	}
	public StringBuffer getServicesJson() {
		return servicesJson;
	}
	public void setServicesJson(StringBuffer servicesJson) {
		this.servicesJson = servicesJson;
	}

}
