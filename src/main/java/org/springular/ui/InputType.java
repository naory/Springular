package org.springular.ui;

/**
 * Enumeration of supported Html/AngularJS Form Input-Types
 */

public enum InputType {
    text,           // text field input
    label,          // for read-only properties
    email,          // text field input for email address
    number,         // text field input for number
    range,          // number selection from a range (slider control)
    url,            // text field input for URL
    tel,            // text field input for telephone number
    password,       // text field input for password (masked)
    select,         // single-value select
    multiSelect,    // select for multiple values
    checkbox,
    radio,          // single select using radio buttons
    textarea,       // multi line text
    date,           // date selector

    // input types for filtering criteria (use for Filter form)
    dateRange,      // use as a filterType only!!! for a date field to filter by range (from_<field>, and to_<field>)
    numberRange,    // number-range control to filter a number property as a range (from_<field>, and to_<field>)
    none;           // no input
}
