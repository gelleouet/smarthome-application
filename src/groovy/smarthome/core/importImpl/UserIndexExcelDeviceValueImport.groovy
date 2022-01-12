package smarthome.core.importImpl

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.springframework.beans.factory.annotation.Autowired

import smarthome.application.GrandDefiService
import smarthome.application.granddefi.AccountCommand
import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.Chauffage
import smarthome.automation.CompteurIndex
import smarthome.automation.CompteurService
import smarthome.automation.Device
import smarthome.automation.ECS
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.common.Commune
import smarthome.core.DeviceValueImport
import smarthome.core.ExcelUtils
import smarthome.core.ImportCommand
import smarthome.core.SmartHomeException
import smarthome.core.TransactionUtils
import smarthome.security.Profil
import smarthome.security.User

/**
 * Import fichier Excel
 * 
 * Non Thread-safe : pas grave car scope request
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class UserIndexExcelDeviceValueImport implements DeviceValueImport {

	private static final Log log = LogFactory.getLog(this)
	
	@Autowired
	GrandDefiService grandDefiService
	
	@Autowired
	CompteurService compteurService
	
	
	ExcelUtils excelUtils
	List communeList
	List profilList 
	List chauffageList 
	List ecsList 
	
	
	@Override
	void importData(ImportCommand command) throws Exception {
		communeList = Commune.list()
		profilList = Profil.list()
		chauffageList = Chauffage.list()
		ecsList = ECS.list()
		
		try {
			excelUtils = new ExcelUtils(command.data).build()
			Sheet sheet = excelUtils.getSheet("import")
			
			if (!sheet) {
				throw new SmartHomeException("Feuillet 'import' introuvable !")
			}
			
			log.info "Try importing ${sheet.getLastRowNum()} row(s)..."
			
			// 1ère ligne sert d'entete, on démarre à la 2e
			for (int idxRow=1; idxRow <= sheet.getLastRowNum(); idxRow++) {
				Row row = sheet.getRow(idxRow)
				
				// 1. Création des comptes utilisateur et inscription défi
				// Si user existe déjà ou déjà inscrit, cela va déclencher des erreurs
				// donc on lance ce 1er traitement dans une transaction séparée
				AccountCommand account = new AccountCommand()
				account.checkUserExist = false // si existe déjà, ca force quand même l'inscription
				account.defaultAccountLocked = false // pas besoin de validation car c'est importé par admin
				
				account.profil = findProfil(excelUtils.getStringCellValue(row, "A"))
				account.nom = excelUtils.getStringCellValue(row, "B")
				account.prenom = "-"
				String communeName = excelUtils.getStringCellValue(row, "C")
				account.commune = findCommune(communeName)
				account.username = excelUtils.getStringCellValue(row, "D")
				account.newPassword = excelUtils.getStringCellValue(row, "E")
				account.confirmPassword = excelUtils.getStringCellValue(row, "F")
				account.surface = excelUtils.getLongCellValue(row, "G")
				account.chauffage = findChauffage(excelUtils.getStringCellValue(row, "H"))
				account.chauffageSecondaire = findChauffage(excelUtils.getStringCellValue(row, "I"))
				account.ecs = findECS(excelUtils.getStringCellValue(row, "J"))
				
				if (!account.commune) {
					throw new SmartHomeException("Commune '$communeName' introuvable ! (ligne ${idxRow+1})")
				}
				
				if (account.username) {
					TransactionUtils.withNewTransaction(User) {
						try {
							grandDefiService.createAccount(account)
						} catch (SmartHomeException ex1) {
							log.warn("Import user ${account.username} : ${ex1.message}")
						}
						
					}
					
					User user = User.findByUsername(account.username)
					
					if (!user) {
						throw new SmartHomeException("Utilisateur '${account.username}' introuvable ! (ligne ${idxRow+1})")
					}
					
					RegisterCompteurCommand compteurCommand = new RegisterCompteurCommand(user: user)
					
					// Ajout des index elec
					List indexElec = []
					ajoutIndex(row, "L", "M", indexElec)
					ajoutIndex(row, "N", "O", indexElec)
					ajoutIndex(row, "P", "Q", indexElec)
					ajoutIndex(row, "R", "S", indexElec)
					ajoutIndex(row, "T", "U", indexElec)
					compteurCommand.compteurModel = TeleInformation.DEFAULT_MODELE
					
					if (indexElec) {
						TransactionUtils.withNewTransaction(Device) {
							try {
								Device compteurElec = compteurService.registerCompteurElec(compteurCommand)
								registerIndex(compteurElec, indexElec)
							} catch (SmartHomeException ex1) {
								log.warn("Import index elec ${account.username} : ${ex1.message}")
							}
						}
					}
					
					// Ajout des index gaz
					List indexGaz = []
					String coefGaz = excelUtils.getStringCellValue(row, "K")
					ajoutIndex(row, "V", "W", indexGaz, coefGaz)
					ajoutIndex(row, "X", "Y", indexGaz, coefGaz)
					ajoutIndex(row, "Z", "AA", indexGaz, coefGaz)
					ajoutIndex(row, "AB", "AC", indexGaz, coefGaz)
					ajoutIndex(row, "AD", "AE", indexGaz, coefGaz)
					compteurCommand.compteurModel = CompteurGaz.DEFAULT_MODELE
					
					if (indexGaz) {
						TransactionUtils.withNewTransaction(Device) {
							try {
								Device compteurGaz = compteurService.registerCompteurGaz(compteurCommand)
								registerIndex(compteurGaz, indexGaz)
							} catch (SmartHomeException ex1) {
								log.warn("Import index gaz ${account.username} : ${ex1.message}")
							}
						}
					}
					
					// Ajout des index eau
					List indexEau = []
					ajoutIndex(row, "AF", "AG", indexEau)
					ajoutIndex(row, "AH", "AI", indexEau)
					ajoutIndex(row, "AJ", "AK", indexEau)
					ajoutIndex(row, "AL", "AM", indexEau)
					ajoutIndex(row, "AN", "AO", indexEau)
					compteurCommand.compteurModel = null
					
					if (indexEau) {
						TransactionUtils.withNewTransaction(Device) {
							try {
								Device compteurEau = compteurService.registerCompteurEau(compteurCommand)
								registerIndex(compteurEau, indexEau)
							} catch (SmartHomeException ex1) {
								log.warn("Import index eau ${account.username} : ${ex1.message}")
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			throw ex 
		} finally {
			excelUtils?.close()
		}
	}
	
	
	private Profil findProfil(String profilName) {
		profilList.find { it.libelle == profilName }
	}
	
	private Commune findCommune(String communeName) {
		communeList.find { it.libelle == communeName }
	}
	
	private Chauffage findChauffage(String chauffageName) {
		chauffageList.find { it.libelle == chauffageName }
	}
	
	private ECS findECS(String ecsName) {
		ecsList.find { it.libelle == ecsName }
	}
	
	private CompteurIndex ajoutIndex(Row row, String idxCellDate, String idxCellIndex, Collection indexList, String param = null) {
		Date dateIndex = excelUtils.getDateCellValue(row, idxCellDate)
		Double valueIndex = excelUtils.getCellValue(row, idxCellIndex)
		CompteurIndex compteurIndex
		
		if (dateIndex && valueIndex) {
			compteurIndex = new CompteurIndex()
			compteurIndex.dateIndex = dateIndex
			// elec : les index sont fournis en kWh mais il doit être enregistré en Wh (x 1000)
			// eau/gaz : les index sont fournis en m3 mais il doit être enregistré en dm3 (x 1000)
			compteurIndex.index1 = valueIndex * 1000
			compteurIndex.param1 = param
			
			indexList << compteurIndex
		}
		
		return compteurIndex
	}
	
	private registerIndex(Device device, Collection indexList) {
		indexList?.each { compteurIndex ->
			compteurIndex.device = device
			compteurService.save(compteurIndex)
		}
	}	
	
}
