package com.group48.blackbox.Test;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class RaysTest {

    @Mock
    private Atoms atoms;
    private Rays rays;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        rays = new Rays(atoms);
    }

    @Test
    public void testRayHitsAtom() {
        when(atoms.hitByRay(anyInt(), anyInt())).thenReturn(true);
        rays.castRay(5, 5);
        assertTrue(rays.didHitAtom());
    }
}
