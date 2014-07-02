package ut.com.cobalt.cdpipeline;

import org.junit.Test;
import com.cobalt.cdpipeline.MyPluginComponent;
import com.cobalt.cdpipeline.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}