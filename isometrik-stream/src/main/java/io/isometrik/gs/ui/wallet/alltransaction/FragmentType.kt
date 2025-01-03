package io.isometrik.gs.ui.wallet.alltransaction


enum class FragmentType(val value: Int) {
    ALL(0),
    CREDIT(1),
    DEBIT(2);

}
public fun getFragmentTypeFromInt(type: Int): FragmentType? {
    return FragmentType.values().find { it.value == type }
}