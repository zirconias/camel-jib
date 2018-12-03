package com.example.demo;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, OptionalChecks.class})
public interface AllChecks {
}
