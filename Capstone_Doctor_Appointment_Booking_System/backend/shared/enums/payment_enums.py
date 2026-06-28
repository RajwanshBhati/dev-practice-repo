from enum import Enum

class PaymentMethod(str, Enum):
    CREDIT_CARD = "CREDIT_CARD"
    DEBIT_CARD = "DEBIT_CARD"
    UPI = "UPI"
    NET_BANKING = "NET_BANKING"
    WALLET = "WALLET"

    @classmethod
    def choices(cls):
        return [(method.value, method.name) for method in cls]

class PaymentGateway(str, Enum):
    STRIPE = "STRIPE"
    RAZORPAY = "RAZORPAY"
    PAYPAL = "PAYPAL"

    @classmethod
    def choices(cls):
        return [(gateway.value, gateway.name) for gateway in cls]
