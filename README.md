# base-converter

A common utility to help other projects to leverage the conversion from one Type to another. For example, Bussiness objects could be different from the Entity object. The classes which converts TypeA to TypeB in most generic sense can be "Converters".

<h3>When can I use this library?</h3>
This library can be used anywhere there is a requirement of converting TypeA to TypeB. For example, there is a need to save details of a Person to a database.
If normal form is followed, the POJO representing the Entity would look like

```
public class Person {
  private String firstName;
  private String lastName;
  // Getters
  // Setters
  // Hashcode, equals
}
```

Now, for reporting, we may have to create a VO which looks like : 

```
public class PersonVO {
  private String fullName;
  // Getters
  // Setters
  // Hashcode, equals
}
```

The code which gets from Person and sets it to PersonVO would be just a bunch of getters & setters. This library is the result of an idea which was lead by a lot of type changing.

The bottom-line is - This library is useful when there are a lot of Type changaing is involved.

<h3>How much meta-data to be added to the project which tries to use this library?</h3>
Minimal!
<ul>
<li>the class should extend `AbstractConverter` and implement the `doConvert` method.</li>
<li>The class should be annotated with @Convert(target=TargetType.class, source=SourceType.class)</li>
<li>Create an instance of BaseConverter and start using Instance of BaseConverter.
</ul>

<h3>How to use this library?</h3>
1. Create Converter, annotate the with @Convert
```
package com.dibya.converter;

@Convert(source = com.dibya.Test.class, target = com.dibya.TestVo.class)
public class TestVoFromTestConverter extends AbstractConverter {
    @Override
    public <T, S> T doConvert(S sourceObject) {
        Test source = (Test) sourceObject;
        TestVo target = new TestVo();
        target.setFullName(source.getFirstName() + " " + source.getLastName());
        target.setAddress(converter.convert(new AddressVo(), source.getAddress());
        return (T) target;
    }
}

package com.dibya.converter.address;
@Convert(source = com.dibya.Address.class, target = com.dibya.AddressVo.class)
public class AddressVoFromAddressConverter extends AbstraactConverter {
    @Override
     public <T, S> T doConvert(S sourceObject) {
         // do stuff
     }
}
```
2. Use created converter by creating an instance of BaseConverter and passing the package which contains the converters
```
public class App {
    public static void main(String... args) {
        Test source = new Test();
        
        // The string argument tells which package to scan for getting the converters
        Converter converter = new BaseConverter("com.dibya.converter"); 
        
        //set the props
        TestVo testVo = converter.convert(new TestVo(), source);
    }
}

```
Now users of this are forced to use AbstractConverter, the users inherit `converter` object that can be used to chain other type changing required inside a Converter.

<h2>Tips</h2>
<ol>
<li>Use a dependency injection framework to inject BaseConverter, maintain it as a singleton. Scanning the packages is costly.</li>
<li>Keep all the converters under one parent package. The search will be quicker</li>
<li>The scanner can scan the sub-packages as well</li>
</ol>

