package cz.cvut.kbss.spipes.test.service

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
  */
//@RunWith(classOf[SpringJUnit4ClassRunner])
class ScriptServiceNoImportsTest extends ScriptServiceTest {

  /*@Test
  def moduleTypesGotFailureFileNotFound: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Failure(new FileNotFoundException()))
    assertEquals(Right(None), service.getModuleTypes(filePath))
  }

  @Test
  def moduleTypesGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Failure(e))
    assertEquals(Left(e), service.getModuleTypes(filePath))
  }

  @Test
  def moduleTypesGotNullSuccess: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(null))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModuleTypes(filePath))
  }

  @Test
  def moduleTypesGotEmptySuccess: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(new util.LinkedList[ModuleType]()))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModuleTypes(filePath))
  }

  @Test
  def moduleTypesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[ModuleType]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new ModuleType()))
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(l))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(Some(l.asScala)), service.getModuleTypes(filePath))
  }

  @Test
  def modulesGotFailureFileNotFound: Unit = {
    when(dao.getModules(false)).thenReturn((_: String) => Failure(new FileNotFoundException()))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModules(filePath))
  }

  @Test
  def modulesGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(dao.getModules(false)).thenReturn((_: String) => Failure(e))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Left(e), service.getModules(filePath))
  }

  @Test
  def modulesGotEmptySuccess: Unit = {
    when(dao.getModules(false)).thenReturn((_: String) => Success(new util.LinkedList[Module]()))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModules(filePath))
  }

  @Test
  def modulesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[Module]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new Module()))
    when(dao.getModules(false)).thenReturn((_: String) => Success(l))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(Some(l.asScala)), service.getModules(filePath))
  }*/

}
