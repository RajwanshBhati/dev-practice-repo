from enum import Enum

class PaymentMethod(str, Enum):
    """Payment options a user can choose from at checkout."""
    CREDIT_CARD = "CREDIT_CARD"
    DEBIT_CARD = "DEBIT_CARD"
    UPI = "UPI"
    NET_BANKING = "NET_BANKING"
    WALLET = "WALLET"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(method.value, method.name) for method in cls]


class PaymentGateway(str, Enum):
    """Third-party payment providers integrated with the app."""
    STRIPE = "STRIPE"
    RAZORPAY = "RAZORPAY"
    PAYPAL = "PAYPAL"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(gateway.value, gateway.name) for gateway in cls]
