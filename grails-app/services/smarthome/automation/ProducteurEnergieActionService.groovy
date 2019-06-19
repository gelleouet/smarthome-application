package smarthome.automation

import org.hibernate.sql.JoinType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional

import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.ProductionInvestissementChart
import smarthome.security.User


class ProducteurEnergieActionService extends AbstractService {

	/**
	 * Liste les actions des producteurs d'énergie d'un utilisateur
	 * 
	 * @param user
	 * @param pagination
	 * @return
	 */
	List<ProducteurEnergieAction> listByUser(User user, ProducteurEnergieActionCommand command, Map pagination) {
		return ProducteurEnergieAction.createCriteria().list(pagination) {
			createAlias 'producteur', 'producteur'
			createAlias 'device', 'device', JoinType.LEFT_OUTER_JOIN.joinTypeValue

			eq 'user', user

			if (command?.producteur) {
				ilike 'producteur.libelle', QueryUtils.decorateMatchAll(command.producteur)
			}

			order(command?.sort ?: 'producteur.libelle', command?.order ?: 'asc')
		}
	}


	/**
	 * Nombre total d'actions d'un user
	 * 
	 * @param user
	 * @return
	 */
	Long totalAction(User user) {
		return ProducteurEnergieAction.createCriteria().get() {
			eq 'user', user
			projections {
				sum 'nbaction'
			}
		}
	}


	/**
	 * Surface totale d'un user
	 *
	 * @param user
	 * @return
	 */
	Double totalSurface(User user) {
		def results = ProducteurEnergieAction.executeQuery("""
SELECT sum(a.nbaction * p.surface / p.nbaction)
FROM ProducteurEnergieAction a
JOIN a.producteur p
WHERE a.user = :user
""", [user: user])

		return results ? results[0] : null
	}


	/**
	 * Investissement total d'un user
	 *
	 * @param user
	 * @return
	 */
	Double totalInvestissement(User user) {
		def results = ProducteurEnergieAction.executeQuery("""
SELECT sum(a.nbaction * p.investissement / p.nbaction)
FROM ProducteurEnergieAction a
JOIN a.producteur p
WHERE a.user = :user
""", [user: user])

		return results ? results[0] : null
	}


	/**
	 * Edition d'un device
	 *
	 * @param device
	 * @return
	 */
	@PreAuthorize("hasPermission(#action, 'OWNER')")
	ProducteurEnergieAction edit(ProducteurEnergieAction action) {
		return action
	}


	/**
	 * Construction d'un chart avec détail des productions
	 * 
	 * @param user
	 * @param command
	 * @return
	 * @throws SmarthomeException
	 */
	GoogleChart chartProduction(User user, ProductionChartCommand command) throws SmartHomeException {
		command.navigation()
		command.actions = this.listByUser(user, null, [:])
		ProductionInvestissementChart chart = new ProductionInvestissementChart(command)
		return chart.build()
	}
}
