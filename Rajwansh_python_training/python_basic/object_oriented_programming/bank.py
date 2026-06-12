"""
Question 43:
Implement encapsulation using private variables in Bank class.
"""


class Bank:
    """Manage bank balance using private variable."""

    def __init__(self, account_holder_name: str, balance: float) -> None:
        # Store account holder name as public information.
        self.account_holder_name = account_holder_name

        # Use private variable to protect balance from direct access.
        self.__balance = balance

    def deposit_amount(self, amount: float) -> None:
        """Add amount to the bank balance."""

        if amount > 0:
            self.__balance += amount

    def withdraw_amount(self, amount: float) -> None:
        """Withdraw amount from the bank balance."""

        if 0 < amount <= self.__balance:
            self.__balance -= amount
        else:
            print("Insufficient balance or invalid amount.")

    def display_balance(self) -> None:
        """Display current bank balance."""

        print(f"Account Holder: {self.account_holder_name}")
        print(f"Balance: {self.__balance}")


def main() -> None:
    """Program entry point."""

    bank_account = Bank("Rajwansh", 5000.0)

    bank_account.deposit_amount(1500.0)
    bank_account.withdraw_amount(1000.0)
    bank_account.display_balance()


if __name__ == "__main__":
    main()
